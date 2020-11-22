package com.sudzusama.vkimageclassifier.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    ): View? {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupsAdapter(requireContext()) { viewModel.onGroupClicked(it) }
        binding.rvGroups.layoutManager = LinearLayoutManager(activity)
        binding.rvGroups.adapter = this.adapter

        binding.btnSignOut.setOnClickListener {
            viewModel.onSignOutButtonClicked()
        }

        viewModel.groups.observe(viewLifecycleOwner, {
            adapter.setGroups(it)
        })

        viewModel.onCreate()
    }


}