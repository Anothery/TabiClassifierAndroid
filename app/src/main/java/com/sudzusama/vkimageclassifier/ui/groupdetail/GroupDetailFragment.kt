package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentGroupDetailBinding
import com.sudzusama.vkimageclassifier.domain.model.GroupTypes
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.ui.createpost.CreatePostFragment
import com.sudzusama.vkimageclassifier.ui.createpost.CreatePostFragment.Companion.ON_POST_CREATED
import com.sudzusama.vkimageclassifier.ui.groupdetail.header.HeaderAdapter
import com.sudzusama.vkimageclassifier.ui.groupdetail.wall.WallAdapter
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetailParentFragment
import com.sudzusama.vkimageclassifier.utils.view.VkSpannableHelper
import com.sudzusama.vkimageclassifier.utils.view.gone
import com.sudzusama.vkimageclassifier.utils.view.shortToast
import com.sudzusama.vkimageclassifier.utils.view.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailFragment : Fragment(R.layout.fragment_group_detail) {
    private val binding by viewBinding(FragmentGroupDetailBinding::bind)
    private val viewModel: GroupDetailViewModel by viewModels()
    private var wallAdapter: WallAdapter? = null
    private var headerAdapter: HeaderAdapter? = null

    companion object {
        const val GROUP_ID = "groupId"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailRecyclerView()

        arguments?.getInt(GROUP_ID)?.let(viewModel::initialize)

        activity?.supportFragmentManager?.setFragmentResultListener(ON_POST_CREATED,
            viewLifecycleOwner, { requestKey, result -> viewModel.onUpdateWall() })


        binding.btnBack.setOnClickListener {
            activity?.findNavController(R.id.navHostFragment)?.popBackStack()
        }

        binding.fabCreate.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).build().send {
                if (it.allGranted()) {
                    viewModel.onFabCreateClicked()
                }
            }
        }

        viewModel.showCreateScreen.observe(viewLifecycleOwner, { detail ->
            activity?.supportFragmentManager?.let {
                CreatePostFragment.newInstance(detail, null).show(it, CreatePostFragment.TAG)
            }
        })

        viewModel.details.observe(viewLifecycleOwner, {
            wallAdapter?.showDeletePrompt(it.isAdmin) // TODO ADD POSIBILITY TO DELETE SELF POSTS
            binding.fabCreate.visible()
            if (!it.canPost) {
                if (it.type == GroupTypes.PAGE) {
                    TooltipCompat.setTooltipText(
                        binding.fabCreate as View,
                        getString(R.string.suggest_post)
                    )
                    binding.fabCreate.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.outline_idea_24
                        )
                    )
                } else {
                    binding.fabCreate.gone()
                    binding.fabCreate.isEnabled = false
                }
            } else {
                TooltipCompat.setTooltipText(
                    binding.fabCreate as View,
                    getString(R.string.create_post)
                )
                binding.fabCreate.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_create_black_24dp
                    )
                )
            }

            binding.btnSettings.visibility = if (it.isAdmin) View.VISIBLE else View.GONE
            binding.btnSettings.setOnClickListener { context?.shortToast(getString(R.string.you_are_admin)) }
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
            wallAdapter?.setWall(newList)
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

        viewModel.showMessage.observe(viewLifecycleOwner, { requireContext().shortToast(it) })

    }

    private fun initDetailRecyclerView() {
        wallAdapter = WallAdapter(
            arrayListOf<WallItem?>().apply { viewModel.wallItems.value?.mapTo(this) { it } },
            requireContext(),
            Glide.with(this),
            VkSpannableHelper(requireContext()),
            viewModel::onPostLiked,
            { images, pos ->
                val detailsFragment = ImageDetailParentFragment.newInstance(images, pos)
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.navHostFragment, detailsFragment, ImageDetailParentFragment.TAG)
                    .commit()
            }, viewModel::onDownloadMore, viewModel::onPostRemoved
        )
        binding.rvWall.setItemViewCacheSize(4)
        headerAdapter = HeaderAdapter(Glide.with(this))
        binding.rvWall.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvWall.adapter = ConcatAdapter(headerAdapter, wallAdapter)
        binding.rvWall.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (binding.fabCreate.isEnabled) {
                    if (dy < 0 && !binding.fabCreate.isShown)
                        binding.fabCreate.show()
                    else if (dy > 0 && binding.fabCreate.isShown)
                        binding.fabCreate.hide()
                }
            }
        })
    }

}