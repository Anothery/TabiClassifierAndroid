package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sudzusama.vkimageclassifier.databinding.ImageDetailItemBinding
import com.sudzusama.vkimageclassifier.utils.ext.addGravity
import com.sudzusama.vkimageclassifier.utils.ext.getStatusBarHeight
import com.sudzusama.vkimageclassifier.utils.ext.removeGravity

class ImageViewPagerAdapter(
    private val images: List<ImageDetail>,
    private val indexToAnimate: Int,
    private val context: Context,
    private val glide: RequestManager,
    private val onAppearingStart: () -> Unit,
    private val onAppearingFinished: () -> Unit
) : RecyclerView.Adapter<ImageViewPagerAdapter.ImagePagerViewHolder>() {

    private var recyclerView: RecyclerView? = null
    private var appearAnimationFinished = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePagerViewHolder {
        val binding =
            ImageDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagePagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int = images.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun showDisappearAnimation(position: Int, onDisappearFinsihed: () -> Unit) {
        recyclerView?.let {
            (it.findViewHolderForAdapterPosition(position) as? ImagePagerViewHolder)
                ?.disappear(images[position], onDisappearFinsihed) ?: onDisappearFinsihed
        }
    }

    inner class ImagePagerViewHolder(
        val binding: ImageDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var widthScale: Float = 0f
        private var heightScale: Float = 0f
        private var finalCenterY: Float = 0f

        fun bind(image: ImageDetail, position: Int) {
            binding.ivImage.maximumScale = binding.ivImage.mediumScale + 0.00001f
            binding.ivImage.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(p0: MotionEvent?) = false
                private var isMax = false
                override fun onDoubleTap(p0: MotionEvent?): Boolean {
                    if (!isMax) {
                        binding.ivImage.setScale(binding.ivImage.maximumScale, true)
                        isMax = true
                    } else {
                        binding.ivImage.setScale(binding.ivImage.minimumScale, true)
                        isMax = false
                    }
                    return false
                }

                override fun onDoubleTapEvent(p0: MotionEvent?) = false
            })

            if (indexToAnimate != position) {
                binding.ivImage.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            glide.load(image.url).listener(object : RequestListener<Drawable> {
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
                        widthScale = 1f * image.width / resource.intrinsicWidth
                        heightScale = 1f * image.height / resource.intrinsicHeight
                        val offsetY = context.getStatusBarHeight()
                        finalCenterY = (binding.root.height - resource.intrinsicHeight) / 2f

                        if (position == indexToAnimate && !appearAnimationFinished) {
                            binding.blackScreen.alpha = 0f
                            binding.ivImage.alpha = 0f
                            binding.ivImage.removeGravity()
                            binding.ivImage.pivotX = 0f
                            binding.ivImage.pivotY = 0f
                            binding.ivImage.scaleX = widthScale
                            binding.ivImage.scaleY = heightScale
                            binding.ivImage.translationX = image.x.toFloat()
                            binding.ivImage.translationY = image.y.toFloat() - offsetY

                            binding.ivImage.animate()
                                .withStartAction {
                                    onAppearingStart()
                                    binding.blackScreen.animate().alpha(1f)
                                }.withEndAction {
                                    binding.ivImage.translationY = 0f
                                    binding.ivImage.addGravity(Gravity.CENTER)
                                    binding.ivImage.layoutParams.height =
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    appearAnimationFinished = true
                                    onAppearingFinished()
                                }
                                .alpha(1f)
                                .scaleX(1f)
                                .scaleY(1f)
                                .translationX(0f)
                                .translationY(finalCenterY)
                                .interpolator = DecelerateInterpolator()
                        }
                    }
                    return false
                }

            }).into(binding.ivImage)
        }

        fun disappear(image: ImageDetail, onDisappearFinished: () -> Unit) {
            binding.ivImage.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.ivImage.removeGravity()
            binding.ivImage.translationY = finalCenterY
            binding.ivImage.pivotX = 0f
            binding.ivImage.pivotY = 0f
            val offsetY = context.getStatusBarHeight()
            binding.ivImage.animate()
                .withStartAction {
                    binding.blackScreen.animate().alpha(0f)
                }.withEndAction {
                    onDisappearFinished()
                }
                .alpha(0f)
                .scaleX(widthScale)
                .scaleY(heightScale)
                .translationX(image.x.toFloat())
                .translationY(image.y.toFloat() - offsetY)
                .interpolator = DecelerateInterpolator()
        }
    }
}