package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupDetailBinding
import com.sudzusama.vkimageclassifier.ui.groupdetail.header.HeaderAdapter
import com.sudzusama.vkimageclassifier.ui.groupdetail.wall.WallAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_group_detail.*


@AndroidEntryPoint
class GroupDetailFragment : Fragment(R.layout.fragment_group_detail) {
    private val binding by viewBinding(FragmentGroupDetailBinding::bind)
    private val viewModel: GroupDetailViewModel by viewModels()
    private lateinit var wallAdapter: WallAdapter
    private lateinit var headerAdapter: HeaderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailRecyclerView()

        binding.btnBack.setOnClickListener {
            activity?.navHostFragment?.findNavController()?.popBackStack()
        }

        viewModel.details.observe(viewLifecycleOwner, {
            binding.tvTitle.text = it.name

            headerAdapter.setHeaders(listOf(it))
            binding.rvWall.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalScrolled = recyclerView.computeVerticalScrollOffset().toFloat()
                    val heightDoubled = binding.toolbar.height * 2
                    if(totalScrolled <= heightDoubled) {
                        binding.tvTitle.alpha = totalScrolled /  heightDoubled
                    } else binding.tvTitle.alpha = 1f
                }
            })
        })

        viewModel.wallItems.observe(viewLifecycleOwner, { newList ->
            wallAdapter.let { it.setWall(it.getWall().toMutableList().apply { addAll(newList) }) }
        })

        arguments?.getInt("groupId")?.let {
            viewModel.getGroupById(it)
            Handler(Looper.getMainLooper()).postDelayed({ viewModel.getWallItems(it, 0) }, 600)
        }

    }

    private fun initDetailRecyclerView() {
        wallAdapter = WallAdapter(Glide.with(this), viewModel::onPostLiked)
        headerAdapter = HeaderAdapter(Glide.with(this))
        binding.rvWall.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvWall.adapter = ConcatAdapter(headerAdapter, wallAdapter)
    }

}