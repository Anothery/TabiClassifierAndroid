package com.sudzusama.vkimageclassifier.ui.groups

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupsBinding
import com.sudzusama.vkimageclassifier.ui.createpost.CreatePostFragment
import com.sudzusama.vkimageclassifier.utils.view.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.math.round

@AndroidEntryPoint
class GroupsFragment : Fragment(R.layout.fragment_groups) {
    private val binding: FragmentGroupsBinding by viewBinding(FragmentGroupsBinding::bind)

    private val viewModel: GroupsViewModel by viewModels()
    private var adapter: GroupsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.srlGroups.setProgressViewOffset(
                    false,
                    binding.actionBar.height / 2,
                    binding.actionBar.height + 8.toPx
                )
            }
        })

        initDrawer()
        initNavLeftMenu()
        initGroupsList()
        initSearchBar()

        binding.srlGroups.setOnRefreshListener {
            viewModel.onSwipedToRefresh()
            binding.srlGroups.isRefreshing = false
        }

        viewModel.groups.observe(viewLifecycleOwner) { adapter?.setGroups(it) }

        viewModel.showMessage.observe(viewLifecycleOwner) { requireContext().shortToast(it) }

        viewModel.showGroupDetail.observe(viewLifecycleOwner) {
            showGroupDetail(it.id)
            context?.hideKeyboard(view)
        }

        viewModel.loading.observe(viewLifecycleOwner) { if (it) binding.progressBar.visible() else binding.progressBar.gone() }

        viewModel.showSendPost.observe(viewLifecycleOwner) {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).build().send { permStatuses ->
                if (permStatuses.allGranted()) {
                    activity?.supportFragmentManager?.setFragmentResultListener(
                        CreatePostFragment.ON_POST_CREATED,
                        viewLifecycleOwner
                    ) { _, _ -> activity?.finish() }
                    activity?.supportFragmentManager?.let { fm ->
                        CreatePostFragment.newInstance(it.first, it.second)
                            .show(fm, CreatePostFragment.TAG)
                    }
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.avatarMedium).error(R.drawable.group_stub_avatar)
                .circleCrop()
                .into(binding.ivAvatar)
            binding.tvName.text = it.firstName
            binding.tvLastName.text = it.lastName
        }

        viewModel.exit.observe(viewLifecycleOwner) { activity?.finish() }
    }

    private fun showGroupDetail(id: Int) {
        activity?.findNavController(R.id.navHostFragment)
            ?.navigate(GroupsFragmentDirections.actionGroupsFragmentToGroupDetailFragment(id))
    }


    @FlowPreview
    private fun initSearchBar() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.svGroups.getQueryTextChangeStateFlow()
                .debounce(200)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { text ->
                    val list = viewModel.groups.value ?: listOf()
                    if (text.isEmpty()) {
                        adapter?.setGroups(list)
                    } else {
                        adapter?.setGroups(list.filter { it.name.contains(text, true) })
                    }
                    binding.rvGroups.smoothScrollToPosition(0)
                }
        }
    }

    private fun initNavLeftMenu() {
        binding.btnSignOut.setOnClickListener {
            viewModel.onSignOutItemClicked()
        }
    }

    private fun initDrawer() {
        binding.drawerLayout.apply {
            setScrimColor(Color.TRANSPARENT)
            drawerElevation = 0f

            val toggle = object : ActionBarDrawerToggle(
                activity, binding.drawerLayout, binding.innerToolbar,
                R.string.groups_drawer_open, R.string.groups_drawer_close
            ) {
                private val scaleFactor = 8f
                private val maxElevation = 25f
                private val maxRadius = 50f

                override fun onDrawerStateChanged(state: Int) {
                    super.onDrawerStateChanged(state)
                    if (state == DrawerLayout.STATE_SETTLING && !isDrawerOpen(GravityCompat.START)) {
                        view?.let { context?.hideKeyboard(it) }
                    }
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.width * slideOffset

                    with(binding.cvContainerContent) {
                        translationX = slideX
                        scaleX = 1 - (slideOffset / scaleFactor)
                        scaleY = 1 - (slideOffset / scaleFactor)
                        elevation = maxElevation * slideOffset
                        radius = round(maxRadius * slideOffset)
                    }
                }
            }

            toggle.setToolbarNavigationClickListener { view?.let { context?.hideKeyboard(it) } }

            addDrawerListener(toggle)
            toggle.syncState()
        }
    }


    private fun initGroupsList() {
        adapter = GroupsAdapter(Glide.with(this)) { viewModel.onGroupClicked(it, activity?.intent) }
        binding.rvGroups.layoutManager = LinearLayoutManager(activity)
        binding.rvGroups.adapter = this.adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvGroups.adapter = null
    }
}