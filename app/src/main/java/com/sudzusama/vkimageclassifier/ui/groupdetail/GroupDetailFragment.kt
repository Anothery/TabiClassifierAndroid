package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class GroupDetailFragment : Fragment(R.layout.fragment_group_detail) {
    private val binding by viewBinding(FragmentGroupDetailBinding::bind)
    private val viewModel: GroupDetailViewModel by viewModels()
    private var adapter: WallAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWall()



        binding.btnBack.setOnClickListener {
            activity?.navHostFragment?.findNavController()?.popBackStack()
        }

        viewModel.details.observe(viewLifecycleOwner, {
            Glide.with(this).load(it.photo200)
                .error(R.drawable.group_stub_avatar)
                .into(binding.ivGroupAvatar)
            binding.tvGroupName.text = it.name
        })

        viewModel.wallItems.observe(viewLifecycleOwner, { newList ->
            adapter?.let { it.setWall(it.getWall().toMutableList().apply { addAll(newList) }) }
        })

        arguments?.getLong("groupId")?.let {
            viewModel.getGroupById(it)
            Handler(Looper.getMainLooper()).postDelayed({ viewModel.getWallItems(it, 0, 10) }, 800)

        }

    }


    private fun initWall() {
        adapter = WallAdapter(Glide.with(this), requireContext())
        binding.rvWall.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvWall.adapter = adapter
    }

}