package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sudzusama.vkimageclassifier.BuildConfig
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentImageDetailParentBinding
import com.sudzusama.vkimageclassifier.utils.view.OnBackPressedListener
import com.sudzusama.vkimageclassifier.utils.view.shortToast
import com.sudzusama.vkimageclassifier.utils.view.toDp
import com.sudzusama.vkimageclassifier.utils.view.visible
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ImageDetailParentFragment(
    private val images: List<ImageDetail>,
    private val selectedImageIndex: Int
) : DialogFragment(R.layout.fragment_image_detail_parent), OnBackPressedListener {
    private val binding by viewBinding(FragmentImageDetailParentBinding::bind)
    private var adapter: ImageViewPagerAdapter? = null
    private val viewModel: ImageDetailViewModel by viewModels()

    private var isFinishing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageViewPagerAdapter(
            images,
            selectedImageIndex,
            requireContext(),
            Glide.with(this),
            this::onAppearingStart,
            this::onAppearingFinished,
            this::onImageDragStart,
            this::onImageDragFinished,
            this::onSingleClickToImage
        )

        binding.imageViewPager.currentItem
        binding.imageViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    binding.imageViewPager.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    binding.imageViewPager.setBackgroundColor(Color.BLACK)

                }
            }
        })

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnSave.setOnClickListener { onSaveButtonClicked() }
        binding.btnShare.setOnClickListener { onShareButtonClicked() }
        hideToolbar()
        binding.imageViewPager.setPageTransformer(MarginPageTransformer(24.toDp(requireContext())))
        binding.imageViewPager.adapter = adapter

        binding.imageViewPager.doOnPreDraw {
            binding.imageViewPager.setCurrentItem(selectedImageIndex, false)
        }

        viewModel.showMessage.observe(viewLifecycleOwner) { activity?.shortToast(it) }
        viewModel.shareImage.observe(viewLifecycleOwner, this::shareImage)
        setBlackStatusBar()
    }

    private fun shareImage(path: String) {
        context?.let { ctx ->
            val file = File(path)
            val contentUri =
                FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".fileprovider", file)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(contentUri, ctx.contentResolver.getType(contentUri))
                putExtra(Intent.EXTRA_STREAM, contentUri)
                type = "image/*"
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_an_app)))
        }
    }

    private fun onShareButtonClicked() {
        val picture = images[binding.imageViewPager.currentItem]
        Glide.with(this).asBitmap().load(picture.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    viewModel.onShareClicked(picture.url, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }


    private fun onSaveButtonClicked() {
        val picture = images[binding.imageViewPager.currentItem]
        Glide.with(this).asBitmap().load(picture.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    viewModel.onSaveClicked(picture.url, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

    }

    private fun onImageDragStart() {
        hideToolbar(true)
    }

    private fun onAppearingStart() {
        binding.imageViewPager.visible()
    }

    private fun onAppearingFinished() {
        showToolbar(true)
    }

    private fun onSingleClickToImage() {
        if (!isFinishing) {
            if (binding.toolbar.alpha == 1f) {
                hideToolbar(true)
            } else {
                showToolbar(true)
            }
        }
    }

    private fun onImageDragFinished(draggedAway: Boolean) {
        if (draggedAway) {
            restoreStatusBar()
            isFinishing = true
            adapter?.showDisappearAnimation(binding.imageViewPager.currentItem) { finish() }
        }
    }

    private fun showToolbar(animate: Boolean = false) {
        if (!animate) {
            binding.toolbar.alpha = 1f
            return
        }
        binding.toolbar.translationY = -binding.toolbar.height.toFloat()
        binding.toolbar.alpha = 0f
        binding.toolbar.animate().alpha(1f).translationY(0f)
    }

    private fun hideToolbar(animate: Boolean = false) {
        if (!animate) {
            binding.toolbar.alpha = 0f
            return
        }
        binding.toolbar.animate().alpha(0f).translationY(-binding.toolbar.height.toFloat())
    }

    private fun setBlackStatusBar() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility = 0
        }

        requireActivity().window.statusBarColor = resources.getColor(android.R.color.black)
    }

    private fun restoreStatusBar() {
        requireActivity().window.statusBarColor = resources.getColor(R.color.colorStatusBar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    private fun finish() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onBackPressed() {
        hideToolbar(true)
        restoreStatusBar()
        binding.imageViewPager.setBackgroundColor(Color.TRANSPARENT)
        isFinishing = true
        adapter?.showDisappearAnimation(binding.imageViewPager.currentItem) {
            finish()
        }
    }

    companion object {
        const val TAG = "ImageDetailsParentFragment"

        @JvmStatic
        fun newInstance(images: List<ImageDetail>, selectedImageIndex: Int) =
            ImageDetailParentFragment(images, selectedImageIndex)
    }
}