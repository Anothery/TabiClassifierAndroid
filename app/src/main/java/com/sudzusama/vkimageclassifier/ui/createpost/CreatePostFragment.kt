package com.sudzusama.vkimageclassifier.ui.createpost

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentCreatePostBinding
import com.sudzusama.vkimageclassifier.utils.ext.gone
import com.sudzusama.vkimageclassifier.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreatePostFragment : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null
    private var picturesAdapter: PicturesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPictures()
        viewModel.galleryItems.observe(viewLifecycleOwner, ::initGallery)
        viewModel.pictures.observe(viewLifecycleOwner, ::onPicturesUpdated)
        viewModel.selectedItem.observe(viewLifecycleOwner) { galleryAdapter?.selectItem(it) }
        viewModel.deselectedItem.observe(viewLifecycleOwner) { galleryAdapter?.deselectItem(it) }
        binding.btnClose.setOnClickListener { dismiss() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGallery(items: List<GalleryItem>) {
        binding.rvGallery.layoutManager = GridLayoutManager(context, 3)
        binding.rvGallery.setHasFixedSize(true);
        galleryAdapter =
            GalleryAdapter(ArrayList(items), Glide.with(this)) { item, position ->
                if (binding.rvGallery.isEnabled) viewModel.onGalleryItemClicked(item, position)
            }

        (binding.rvGallery.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        binding.rvGallery.adapter = galleryAdapter
    }

    private fun onPicturesUpdated(pictures: List<Picture>) {
        picturesAdapter?.let { adapter ->
            if (pictures.isNotEmpty() && adapter.itemCount == 0) {
                binding.flEmptyPicture
                    .animate()
                    .alpha(0f)
                    .scaleX(0f)
                    .scaleY(0f)
                    .withEndAction { binding.flEmptyPicture.gone() }
                binding.rvPictures.visible()
                binding.rvPictures.alpha = 0f
                binding.rvPictures.animate().alpha(1f)
            } else if (pictures.isEmpty() && adapter.itemCount != 0) {
                binding.rvGallery.isEnabled = false
                binding.flEmptyPicture.visible()
                binding.flEmptyPicture.alpha = 0f
                binding.flEmptyPicture.scaleX = 0f
                binding.flEmptyPicture.scaleY = 0f
                binding.flEmptyPicture.animate().alpha(1f).scaleX(1f).scaleY(1f)
                binding.rvPictures.animate().alpha(0f).withEndAction {
                    binding.rvPictures.gone()
                    binding.rvPictures.layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
                    binding.rvGallery.isEnabled = true
                }
                adapter.setPictures(pictures)
                return@let
            }

            binding.rvPictures.layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
            adapter.setPictures(pictures)
            if (adapter.itemCount == 1 && pictures.size > 1) {
                binding.rvPictures.layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun initPictures() {
        picturesAdapter = PicturesAdapter(Glide.with(this)) { viewModel.onRemovePictureClicked(it) }
        binding.rvPictures.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPictures.adapter = picturesAdapter
        PagerSnapHelper().attachToRecyclerView(binding.rvPictures)
        binding.rvIndicator.attachToRecyclerView(binding.rvPictures)
    }


    override fun onStart() {
        super.onStart()
        val behaviour = BottomSheetBehavior.from(requireView().parent as View)
        behaviour.peekHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onDestroyView() {
        super.onDestroyView()
        galleryAdapter = null
        picturesAdapter = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    companion object {
        const val TAG = "CreatePostFragment"
        const val GROUP_ID = "GROUP_ID"

        @JvmStatic
        fun newInstance(id: Int) = CreatePostFragment().apply {
            arguments = Bundle().apply { putInt(GROUP_ID, id) }
        }
    }
}