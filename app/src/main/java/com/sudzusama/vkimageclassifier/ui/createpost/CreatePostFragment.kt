package com.sudzusama.vkimageclassifier.ui.createpost

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
import by.kirich1409.viewbindingdelegate.viewBinding
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
    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null
    private var picturesAdapter: PicturesAdapter? = null
    private var genresAdapter: TagsAdapter? = null
    private var colorsAdapter: TagsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setGroupId(arguments?.getInt(GROUP_ID) ?: -1)
        initPictures()
        initGenres()
        initColors()
        viewModel.galleryItems.observe(viewLifecycleOwner, ::initGallery)
        viewModel.pictures.observe(viewLifecycleOwner, ::onPicturesUpdated)
        viewModel.selectedItem.observe(viewLifecycleOwner) { galleryAdapter?.selectItem(it) }
        viewModel.deselectedItem.observe(viewLifecycleOwner) { galleryAdapter?.deselectItem(it) }
        viewModel.genreTags.observe(viewLifecycleOwner) { tags ->
            genresAdapter?.let { adapter ->
                if (adapter.itemCount == 0 && tags.isNotEmpty()) {
                    binding.rvGenreTags.visible()
                    binding.rvGenreTags.alpha = 0f
                    binding.rvGenreTags.animate().alpha(1f).scaleY(1f)
                    binding.rvGenreTags.post { adapter.setTags(tags) }
                } else if (adapter.itemCount != 0 && tags.isEmpty()) {
                    binding.rvGenreTags.animate().alpha(0f).scaleY(0f)
                        .withEndAction {
                            binding.rvGenreTags.gone()
                            binding.rvGenreTags.post { adapter.setTags(tags) }
                        }
                } else binding.rvGenreTags.post { adapter.setTags(tags) }
            }
        }
        viewModel.colorTags.observe(viewLifecycleOwner) { tags ->
            colorsAdapter?.let { adapter ->
                if (adapter.itemCount == 0 && tags.isNotEmpty()) {
                    binding.rvColorTags.post { adapter.setTags(tags) }
                    binding.rvColorTags.visible()
                    binding.rvColorTags.alpha = 0f
                    binding.rvColorTags.animate().alpha(1f)
                } else if (adapter.itemCount != 0 && tags.isEmpty()) {
                    binding.rvGallery.isEnabled = false
                    binding.rvColorTags.animate().alpha(0f)
                        .withEndAction {
                            binding.rvColorTags.gone()
                            binding.rvColorTags.post { adapter.setTags(tags) }
                            binding.rvGallery.isEnabled = true
                        }
                } else binding.rvColorTags.post { adapter.setTags(tags) }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { context?.shortToast(it) }
        viewModel.onPostSent.observe(viewLifecycleOwner) {
            activity?.supportFragmentManager?.setFragmentResult(ON_POST_CREATED, bundleOf())
            dismiss();
        }
        viewModel.tagsRecognition.observe(viewLifecycleOwner) { enabled ->
            binding.btnTagsRecognition.setImageDrawable(
                if (enabled) ContextCompat.getDrawable(
                    requireContext(), R.drawable.outline_blur_on_24
                ) else ContextCompat.getDrawable(requireContext(), R.drawable.outline_blur_off_24)
            )
        }
        binding.btnSave.setOnClickListener { viewModel.onSaveButtonClicked() }
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnSetDefaultTags.setOnClickListener { viewModel.onSetDefaultTagsClicked() }
        binding.btnTagsRecognition.setOnClickListener { viewModel.onChangeTagsRecognition() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGallery(items: List<GalleryItem>) {
        binding.rvGallery.layoutManager = GridLayoutManager(context, 3)
        binding.rvGallery.setHasFixedSize(true)
        galleryAdapter =
            GalleryAdapter(ArrayList(items), Glide.with(this)) { item, position ->
                if (binding.rvGallery.isEnabled) viewModel.onGalleryItemClicked(item, position)
            }

        (binding.rvGallery.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        binding.rvGallery.adapter = galleryAdapter
    }

    private fun initColors() {
        colorsAdapter = TagsAdapter(viewModel::onColorCheckUpdate)
        binding.rvColorTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvColorTags.adapter = colorsAdapter
    }

    private fun initGenres() {
        genresAdapter = TagsAdapter(viewModel::onGenreCheckUpdate)
        binding.rvGenreTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvGenreTags.adapter = genresAdapter
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
        picturesAdapter =
            PicturesAdapter(Glide.with(this), viewModel::onRemovePictureClicked, requireContext())
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
        genresAdapter = null
        colorsAdapter = null
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