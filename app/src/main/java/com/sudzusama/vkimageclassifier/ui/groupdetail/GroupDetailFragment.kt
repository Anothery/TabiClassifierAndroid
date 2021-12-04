package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetailParentFragment
import com.sudzusama.vkimageclassifier.utils.ext.shortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailFragment : Fragment(R.layout.fragment_group_detail) {
    private val binding by viewBinding(FragmentGroupDetailBinding::bind)
    private val viewModel: GroupDetailViewModel by viewModels()
    private var wallAdapter: WallAdapter? = null
    private var headerAdapter: HeaderAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailRecyclerView()

        binding.btnBack.setOnClickListener {
            activity?.findNavController(R.id.navHostFragment)?.popBackStack()
        }

        viewModel.details.observe(viewLifecycleOwner, {
            binding.btnSettings.visibility = if (it.isAdmin) View.VISIBLE else View.GONE
            binding.tvTitle.text = it.name
            headerAdapter?.setHeaders(listOf(it))
            binding.rvWall.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalScrolled = recyclerView.computeVerticalScrollOffset().toFloat()
                    val heightDoubled = binding.toolbar.height * 2
                    if (totalScrolled <= heightDoubled) {
                        binding.tvTitle.alpha = totalScrolled / heightDoubled
                    } else binding.tvTitle.alpha = 1f
                }
            })
        })

        viewModel.wallItems.observe(viewLifecycleOwner, { newList ->
            if (newList.isEmpty()) {
                binding.tvEmptyWall.visibility = View.VISIBLE
            } else {
                binding.tvEmptyWall.visibility = View.GONE
            }
            wallAdapter?.let { it.setWall(it.getWall().toMutableList().apply { addAll(newList) }) }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            wallAdapter?.setIsDataLoading(isLoading)
        })

        viewModel.downloadMore.observe(viewLifecycleOwner, { downloadMore ->
            wallAdapter?.setDownloadMore(downloadMore)
        })

        viewModel.showStartProgress.observe(viewLifecycleOwner, { showStartProgress ->
            if (!showStartProgress) binding.progressBar.visibility = View.GONE
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, { requireContext().shortToast(it) })

        arguments?.getInt("groupId")?.let {
            viewModel.initWallItem(it)
            viewModel.getGroupById()
            Handler(Looper.getMainLooper()).postDelayed({ viewModel.getFirstWallItems() }, 600)
        }

    }

    private fun initDetailRecyclerView() {
        wallAdapter = WallAdapter(
            Glide.with(this),
            viewModel::onPostLiked,
            { images, pos ->
                val detailsFragment = ImageDetailParentFragment.newInstance(images, pos)
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.navHostFragment, detailsFragment, ImageDetailParentFragment.TAG)
                    .commit()
            }, viewModel::onDownloadMore
        )
        headerAdapter = HeaderAdapter(Glide.with(this))
        binding.rvWall.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvWall.adapter = ConcatAdapter(headerAdapter, wallAdapter)
    }

}