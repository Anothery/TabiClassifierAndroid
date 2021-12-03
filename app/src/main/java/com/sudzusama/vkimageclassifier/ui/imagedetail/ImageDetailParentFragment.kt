package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentImageDetailParentBinding
import com.sudzusama.vkimageclassifier.utils.OnBackPressedListener
import com.sudzusama.vkimageclassifier.utils.ext.toDp

class ImageDetailParentFragment(
    private val images: List<ImageDetail>,
    private val selectedImageIndex: Int
) : DialogFragment(R.layout.fragment_image_detail_parent), OnBackPressedListener {
    private val binding by viewBinding(FragmentImageDetailParentBinding::bind)
    private var adapter: ImageViewPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            ImageViewPagerAdapter(
                images,
                selectedImageIndex,
                requireContext(),
                Glide.with(this),
                {
                    binding.imageViewPager.visibility = View.VISIBLE
                }, {
                    binding.imageViewPager.setBackgroundColor(Color.BLACK)
                })

        binding.imageViewPager.visibility = View.INVISIBLE
        binding.imageViewPager.setPageTransformer(MarginPageTransformer(24.toDp(requireContext())))
        binding.imageViewPager.adapter = adapter

        binding.imageViewPager.doOnPreDraw {
            binding.imageViewPager.setCurrentItem(selectedImageIndex, false)
        }
        setBlackStatusBar()
    }

    private fun setBlackStatusBar() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = resources.getColor(android.R.color.black)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility = 0
        }
    }

    private fun restoreStatusBar() {
        requireActivity().window.statusBarColor = resources.getColor(R.color.colorStatusBar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    companion object {
        const val TAG = "ImageDetailsParentFragment"

        @JvmStatic
        fun newInstance(images: List<ImageDetail>, selectedImageIndex: Int) =
            ImageDetailParentFragment(images, selectedImageIndex)
    }

    override fun onBackPressed() {
        restoreStatusBar()
        binding.imageViewPager.setBackgroundColor(Color.TRANSPARENT)
        adapter?.showDisappearAnimation(binding.imageViewPager.currentItem) {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}