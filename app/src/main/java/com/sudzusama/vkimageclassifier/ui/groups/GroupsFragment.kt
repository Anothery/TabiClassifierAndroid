package com.sudzusama.vkimageclassifier.ui.groups

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupsBinding
import com.sudzusama.vkimageclassifier.utils.ext.getQueryTextChangeStateFlow
import com.sudzusama.vkimageclassifier.utils.ext.hideKeyboard
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
class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupsViewModel by viewModels()
    private var adapter: GroupsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDrawer()
        initNavLeftMenu()
        initGroupsList()
        initSearchBar()

        viewModel.groups.observe(viewLifecycleOwner, {
            adapter?.setGroups(it)
        })
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
                        activity?.hideKeyboard()
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

            toggle.setToolbarNavigationClickListener { activity?.hideKeyboard() }

            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initGroupsList() {
        adapter = GroupsAdapter(Glide.with(this)) { viewModel.onGroupClicked(it) }
        binding.rvGroups.layoutManager = LinearLayoutManager(activity)
        binding.rvGroups.adapter = this.adapter

        binding.rvGroups.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val appBarHeight = binding.actionBar.height
                binding.rvGroups.also {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    it.translationY = -1f * appBarHeight
                    it.layoutParams.height = binding.rvGroups.height + appBarHeight
                    it.updatePadding(top = appBarHeight)
                    it.clipToPadding = false
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvGroups.adapter = null
        _binding = null
    }
}