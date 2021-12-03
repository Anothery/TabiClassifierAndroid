package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.FragmentImageDetailBinding
import com.sudzusama.vkimageclassifier.utils.OnBackPressedListener
import com.sudzusama.vkimageclassifier.utils.ext.addGravity
import com.sudzusama.vkimageclassifier.utils.ext.removeGravity

class ImageDetailFragment(
    private val url: String,
    private val width: Int,
    private val height: Int,
    private val x: Int,
    private val y: Int
) : DialogFragment(R.layout.fragment_image_detail), OnBackPressedListener {
    private val binding by viewBinding(FragmentImageDetailBinding::bind)

    private var widthScale: Float = 0f
    private var heightScale: Float = 0f
    private var finalCenterY: Float = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBlackStatusBar()

        Glide.with(this).load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    if (resource != null) {
                        binding.ivImage.removeGravity()
                        val ratio = 1f * resource.intrinsicHeight / resource.intrinsicWidth
                        finalCenterY = (view.measuredHeight - resource.intrinsicHeight) / 2f
                        widthScale =
                            this@ImageDetailFragment.height.toFloat() / ratio / resource.intrinsicWidth
                        heightScale =
                            this@ImageDetailFragment.height.toFloat() / resource.intrinsicHeight

                        val offsets = IntArray(2).apply { view.getLocationOnScreen(this) }

                        binding.ivImage.pivotX = 0f
                        binding.ivImage.pivotY = 0f
                        binding.ivImage.scaleX = widthScale
                        binding.ivImage.scaleY = heightScale
                        binding.ivImage.translationX = this@ImageDetailFragment.x.toFloat()
                        binding.ivImage.translationY =
                            this@ImageDetailFragment.y.toFloat() - offsets[1]

                        binding.ivImage.animate()
                            .withStartAction {
                                binding.blackScreen.animate().alpha(1f)
                            }.withEndAction {
                                binding.ivImage.translationY = 0f
                                binding.ivImage.addGravity(Gravity.CENTER)
                            }
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .translationX(0f)
                            .translationY(finalCenterY)
                            .interpolator = DecelerateInterpolator()
                    }

                    return false
                }


            })
            .into(binding.ivImage)
    }

    private fun disappear() {
        restoreStatusBar()
        binding.ivImage.removeGravity()
        binding.ivImage.translationY = finalCenterY
        val offsets = IntArray(2).apply { requireView().getLocationOnScreen(this) }
        binding.ivImage.animate()
            .withStartAction {
                binding.blackScreen.animate().alpha(0f)
            }.withEndAction {
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            }
            .alpha(0f)
            .scaleX(widthScale)
            .scaleY(heightScale)
            .translationX(this@ImageDetailFragment.x.toFloat())
            .translationY(this@ImageDetailFragment.y.toFloat() - offsets[1])
            .interpolator = DecelerateInterpolator()
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
        const val TAG = "ImageDetailsFragment"

        @JvmStatic
        fun newInstance(url: String, width: Int, height: Int, x: Int, y: Int) =
            ImageDetailFragment(url, width, height, x, y)
    }

    override fun onBackPressed() {
        disappear()
    }
}