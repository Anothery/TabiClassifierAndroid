package com.sudzusama.vkimageclassifier.ui.groups

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : Fragment() {
    private lateinit var binding: FragmentGroupsBinding
    private val viewModel: GroupsViewModel by viewModels()
    private lateinit var adapter: GroupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initDrawer()
        initNavLeftMenu()
        initGroupsList()

        viewModel.groups.observe(viewLifecycleOwner, {
            adapter.setGroups(it)
        })

        viewModel.onCreate()
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
                activity,
                binding.drawerLayout, binding.innerToolbar,
                R.string.groups_drawer_open,
                R.string.groups_drawer_close
            ) {
                private val scaleFactor = 8f

                override fun onDrawerStateChanged(newState: Int) {
                    super.onDrawerStateChanged(newState)
                    if (newState == DrawerLayout.STATE_SETTLING) {

                        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) { // is opening

                            this.isDrawerIndicatorEnabled = false
                            binding.innerToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                            this.isDrawerIndicatorEnabled = true

                            showContentElevation()
                        }
                    }
                }

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)

                    this.isDrawerIndicatorEnabled = false
                    binding.innerToolbar.setNavigationIcon(R.drawable.three_bars)
                    this.isDrawerIndicatorEnabled = true

                    hideContentElevation()
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.width * slideOffset
                    with(binding.cvContainerContent) {
                        translationX = slideX
                        scaleX = 1 - (slideOffset / scaleFactor)
                        scaleY = 1 - (slideOffset / scaleFactor)
                    }
                }
            }

            addDrawerListener(toggle)
        }
    }

    private fun initToolbar() {
        with(binding.innerToolbar) {
            title = null
            setNavigationIcon(R.drawable.three_bars)
            setNavigationOnClickListener { toogleDrawer() }
        }
    }


    private fun toogleDrawer() {
        with(binding.drawerLayout) {
            if (this.isDrawerOpen(GravityCompat.START)) {
                this.closeDrawer(GravityCompat.START)
            } else {
                this.openDrawer(GravityCompat.START)
                binding.innerToolbar.setNavigationIcon(R.drawable.ic_arrow_back)

            }
        }
    }

    private fun initGroupsList() {
        adapter = GroupsAdapter(requireContext()) { viewModel.onGroupClicked(it) }
        binding.rvGroups.layoutManager = LinearLayoutManager(activity)
        binding.rvGroups.adapter = this.adapter
    }

    private fun showContentElevation() {
        with(binding.cvContainerContent) {
            elevation = 25f
            radius = 50f
        }
    }

    private fun hideContentElevation() {
        with(binding.cvContainerContent) {
            elevation = 0f
            radius = 0f
        }
    }
}