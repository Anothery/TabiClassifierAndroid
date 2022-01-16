package com.sudzusama.vkimageclassifier.ui.createpost

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentCreatePostBinding
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryAdapter
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.PicturesAdapter
import com.sudzusama.vkimageclassifier.ui.createpost.tags.TagsAdapter
import com.sudzusama.vkimageclassifier.utils.view.gone
import com.sudzusama.vkimageclassifier.utils.view.shortToast
import com.sudzusama.vkimageclassifier.utils.view.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreatePostFragment : BottomSheetDialogFragment() {
    private var binding: FragmentCreatePostBinding? = null
    private val viewModel: CreatePostViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null
    private var picturesAdapter: PicturesAdapter? = null
    private var genresAdapter: TagsAdapter? = null
    private var colorsAdapter: TagsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setGroupId(arguments?.getInt(GROUP_ID) ?: -1)
        initPictures()
        initGenres()
        initColors()
        binding?.btnSave?.setOnClickListener { viewModel.onSaveButtonClicked() }
        binding?.btnClose?.setOnClickListener { dismiss() }
        binding?.btnSetDefaultTags?.setOnClickListener { viewModel.onSetDefaultTagsClicked() }
        binding?.btnTagsRecognition?.setOnClickListener { viewModel.onChangeTagsRecognition() }
        binding?.llMain?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)

        viewModel.galleryItems.observe(viewLifecycleOwner, ::initGallery)
        viewModel.pictures.observe(viewLifecycleOwner, ::onPicturesUpdated)
        viewModel.selectedItem.observe(viewLifecycleOwner) { galleryAdapter?.selectItem(it) }
        viewModel.deselectedItem.observe(viewLifecycleOwner) { galleryAdapter?.deselectItem(it) }

        viewModel.genreTags.observe(viewLifecycleOwner) { tags ->
            genresAdapter?.let { binding?.rvGenreTags?.post { it.setTags(tags) } }
        }
        viewModel.colorTags.observe(viewLifecycleOwner) { tags ->
            colorsAdapter?.let { adapter ->
                val shouldScroll = adapter.itemCount != 0 && tags.isNotEmpty()
                binding?.rvColorTags?.post {
                    adapter.setTags(tags)
                    if (shouldScroll) binding?.rvColorTags?.scrollToPosition(0)
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { context?.shortToast(it) }
        viewModel.onPostSent.observe(viewLifecycleOwner) {
            activity?.supportFragmentManager?.setFragmentResult(ON_POST_CREATED, bundleOf())
            dismiss()
        }
        viewModel.tagsRecognition.observe(viewLifecycleOwner) { enabled ->
            binding?.btnTagsRecognition?.setImageDrawable(
                if (enabled) ContextCompat.getDrawable(
                    requireContext(), R.drawable.outline_blur_on_24
                ) else ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.outline_blur_off_24
                )
            )
        }

        viewModel.postingState.observe(viewLifecycleOwner) { posting ->
            if (posting) disableControls() else enableControls()
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGallery(items: List<GalleryItem>) {
        binding?.rvGallery?.layoutManager = GridLayoutManager(context, 3)
        binding?.rvGallery?.setHasFixedSize(true)
        galleryAdapter =
            GalleryAdapter(ArrayList(items), Glide.with(this)) { item, position ->
                if (binding?.rvGallery?.isEnabled == true) viewModel.onGalleryItemClicked(
                    item,
                    position
                )
            }
        (binding?.rvGallery?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        binding?.rvGallery?.adapter = galleryAdapter
    }

    private fun initColors() {
        colorsAdapter = TagsAdapter(viewModel::onColorCheckUpdate)
        binding?.rvColorTags?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvColorTags?.adapter = colorsAdapter
    }

    private fun initGenres() {
        genresAdapter = TagsAdapter(viewModel::onGenreCheckUpdate)
        binding?.rvGenreTags?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvGenreTags?.adapter = genresAdapter
    }

    private fun onPicturesUpdated(pictures: List<Picture>) {
        picturesAdapter?.let { adapter ->
            val prevItemCount = adapter.itemCount
            if (pictures.isNotEmpty() && adapter.itemCount == 0) {
                binding?.flEmptyPicture?.animate()?.alpha(0f)?.scaleX(0f)?.scaleY(0f)
                    ?.withEndAction { binding?.flEmptyPicture?.gone() }
                binding?.rvPictures?.visible()
                binding?.rvPictures?.alpha = 0f
                binding?.rvPictures?.animate()?.alpha(1f)
            } else if (pictures.isEmpty() && adapter.itemCount != 0) {
                binding?.rvGallery?.isEnabled = false
                binding?.flEmptyPicture?.visible()
                binding?.flEmptyPicture?.alpha = 0f
                binding?.flEmptyPicture?.scaleX = 0f
                binding?.flEmptyPicture?.scaleY = 0f
                binding?.flEmptyPicture?.animate()?.alpha(1f)?.scaleX(1f)?.scaleY(1f)
                binding?.rvPictures?.animate()?.alpha(0f)?.withEndAction {
                    binding?.rvPictures?.gone()
                    binding?.rvPictures?.layoutParams?.width =
                        RecyclerView.LayoutParams.MATCH_PARENT
                    binding?.rvGallery?.isEnabled = true
                }
                adapter.setPictures(pictures)
                if (prevItemCount < pictures.size) {
                    binding?.rvPictures?.scrollToPosition(pictures.lastIndex)
                }
                return@let
            }

            binding?.rvPictures?.layoutParams?.width = RecyclerView.LayoutParams.MATCH_PARENT
            adapter.setPictures(pictures)
            if (prevItemCount < pictures.size) {
                binding?.rvPictures?.scrollToPosition(pictures.lastIndex)
            }
            if (adapter.itemCount == 1 && pictures.size > 1) {
                binding?.rvPictures?.layoutParams?.width = RecyclerView.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun initPictures() {
        picturesAdapter =
            PicturesAdapter(
                Glide.with(this),
                viewModel::onRemovePictureClicked,
                requireContext()
            )
        binding?.rvPictures?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvPictures?.adapter = picturesAdapter
        PagerSnapHelper().attachToRecyclerView(binding?.rvPictures)
        binding?.rvPictures?.let { binding?.rvIndicator?.attachToRecyclerView(it) }
    }

    private fun disableControls() {
        binding?.btnSave?.isEnabled = false
        binding?.btnTagsRecognition?.isEnabled = false
        binding?.btnSetDefaultTags?.isEnabled = false
        binding?.flPosting?.visible()
        binding?.flPosting?.alpha = 0f
        binding?.flPosting?.animate()?.alpha(1f)
    }

    private fun enableControls() {
        binding?.btnSave?.isEnabled = true
        binding?.btnTagsRecognition?.isEnabled = true
        binding?.btnSetDefaultTags?.isEnabled = true
        binding?.flPosting?.animate()?.alpha(0f)?.withEndAction {
            binding?.flPosting?.gone()
        }
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
        genresAdapter = null
        colorsAdapter = null
        binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    companion object {
        const val TAG = "CreatePostFragment"
        const val GROUP_ID = "GROUP_ID"
        const val ON_POST_CREATED = "ON_POST_CREATED"

        @JvmStatic
        fun newInstance(id: Int) = CreatePostFragment().apply {
            arguments = Bundle().apply { putInt(GROUP_ID, id) }
        }
    }
}