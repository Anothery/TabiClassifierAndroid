package com.sudzusama.vkimageclassifier.ui.createpost

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreatePostFragment : BottomSheetDialogFragment() {
    private val binding by viewBinding(FragmentCreatePostBinding::bind)
    private val viewModel: CreatePostViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.galleryItems.observe(viewLifecycleOwner, ::initGallery)
        viewModel.selectedItem.observe(viewLifecycleOwner) { galleryAdapter?.selectItem(it) }
        viewModel.deselectedItem.observe(viewLifecycleOwner) { galleryAdapter?.deselectItem(it) }
        binding.btnClose.setOnClickListener { dismiss() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGallery(items: List<GalleryItem>) {
        binding.rvGallery.layoutManager = GridLayoutManager(context, 3)
        binding.rvGallery.setHasFixedSize(true);
        galleryAdapter =
            GalleryAdapter(ArrayList(items), Glide.with(this), viewModel::onGalleryItemClicked)

        (binding.rvGallery.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        binding.rvGallery.adapter = galleryAdapter
        binding.rvGallery.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event)
            true
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