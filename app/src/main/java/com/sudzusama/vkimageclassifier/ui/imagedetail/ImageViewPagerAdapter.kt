package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
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
import com.sudzusama.vkimageclassifier.utils.ext.toPx
import kotlin.math.abs

class ImageViewPagerAdapter(
    private val images: List<ImageDetail>,
    private val indexToAnimate: Int,
    private val context: Context,
    private val glide: RequestManager,
    private val onAppearingStart: () -> Unit,
    private val onAppearingFinished: () -> Unit,
    private val onImageDragStart: () -> Unit,
    private val onImageDragFinished: (Boolean) -> Unit,
    private val onSingleClick: () -> Unit
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
        private var centeredY: Float = 0f

        private var isMax = false

        @SuppressLint("ClickableViewAccessibility")
        fun bind(image: ImageDetail, position: Int) {
            binding.ivImage.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
            binding.ivImage.maximumScale = binding.ivImage.mediumScale + 0.00001f
            binding.ivImage.setOnTouchListener(OnImageDetailTouchListener())
            binding.ivImage.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(p0: MotionEvent?) = false
                override fun onDoubleTapEvent(p0: MotionEvent?) = false
                override fun onDoubleTap(p0: MotionEvent?): Boolean {
                    if (!isMax) scaleToMax() else scaleToMin()
                    return false
                }
            })
            glide.load(image.url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean = false


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
                        centeredY = (binding.root.height - resource.intrinsicHeight) / 2f

                        if (position != indexToAnimate) binding.ivImage.translationY = centeredY

                        if (position == indexToAnimate && !appearAnimationFinished) {
                            appear(context, image, onAppearingStart, onAppearingFinished)
                        }
                    }
                    return false
                }

            }).into(binding.ivImage)
        }

        fun disappear(image: ImageDetail, onDisappearFinished: () -> Unit) {
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

        fun appear(
            context: Context,
            image: ImageDetail,
            onAppearingStart: () -> Unit,
            onAppearingFinished: () -> Unit
        ) {
            val offsetY = context.getStatusBarHeight()

            binding.blackScreen.alpha = 0f
            binding.ivImage.alpha = 0f
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
                    appearAnimationFinished = true
                    onAppearingFinished()
                }
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0f)
                .translationY(centeredY)
                .interpolator = DecelerateInterpolator()
        }

        private fun scaleToMax() {
            binding.ivImage.setScale(binding.ivImage.maximumScale, true)
            isMax = true
        }

        private fun scaleToMin() {
            binding.ivImage.setScale(binding.ivImage.minimumScale, true)
            isMax = false
        }


        private fun setImageCenteredWithWrapContent() {
            binding.ivImage.y = centeredY
            binding.ivImage.layoutParams.height =
                FrameLayout.LayoutParams.WRAP_CONTENT
            binding.ivImage.removeGravity()
        }

        private fun setImageCenteredWithMatchParent() {
            binding.ivImage.y = 0f
            binding.ivImage.layoutParams.height =
                FrameLayout.LayoutParams.MATCH_PARENT
            binding.ivImage.addGravity(Gravity.CENTER)
        }

        inner class OnImageDetailTouchListener : View.OnTouchListener {
            private val handler = Handler(Looper.getMainLooper())

            private var multiTouchMode = false
            private var singleTouchMode = false

            private var dy: Float = 0f

            // start offset variables
            private var startY: Float = 0f
            private var changedY: Float = 0f
            private var dragOffsetPx = 50.toPx
            private val hideOffsetPx = 120.toPx

            // catch double click
            private val CLICK_ACTION_THRESHOLD = 350
            private val DOUBLE_CLICK_DELTA_MS = 200
            private var checkX = 0f
            private var checkY = 0f
            private var lastTimeClickMs = 0L

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(view: View, e: MotionEvent): Boolean {
                when (e.actionMasked) {
                    MotionEvent.ACTION_UP -> {
                        if (isAClick(checkX, e.x, checkY, e.y)) {
                            val currentTime = System.currentTimeMillis()
                            val diff = currentTime - lastTimeClickMs
                            lastTimeClickMs = currentTime
                            if (diff <= DOUBLE_CLICK_DELTA_MS) {
                                lastTimeClickMs = 0L
                                if (multiTouchMode && binding.ivImage.scale == binding.ivImage.minimumScale) {
                                    multiTouchMode = false
                                }
                                if (!multiTouchMode && !singleTouchMode) {
                                    setImageCenteredWithMatchParent()
                                    scaleToMax()
                                    multiTouchMode = true
                                    view.parent.requestDisallowInterceptTouchEvent(true)
                                }
                            } else {
                                handler.postDelayed({
                                    if (lastTimeClickMs != 0L) onSingleClick()
                                }, DOUBLE_CLICK_DELTA_MS.toLong())
                            }
                        }
                    }
                    MotionEvent.ACTION_DOWN -> {
                        checkX = e.x
                        checkY = e.y
                        startY = 0f
                        changedY = 0f
                        dragOffsetPx = abs(dragOffsetPx)

                        if (binding.ivImage.scale == binding.ivImage.minimumScale && multiTouchMode) {
                            setImageCenteredWithWrapContent()
                            singleTouchMode = false
                            multiTouchMode = false
                            view.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                }

                if (binding.ivImage.scale == binding.ivImage.minimumScale && !multiTouchMode) {
                    when (e.actionMasked) {
                        MotionEvent.ACTION_UP -> {
                            if (singleTouchMode) {
                                if (startY > e.rawY + dy + hideOffsetPx || (startY < e.rawY + dy - hideOffsetPx)) {
                                    onImageDragFinished(true)
                                    binding.ivImage.setOnTouchListener { v, e ->
                                        binding.ivImage.attacher.onTouch(v, e)
                                    }
                                    view.parent.requestDisallowInterceptTouchEvent(false)
                                } else {
                                    onImageDragFinished(false)
                                    singleTouchMode = false
                                    startY = 0f
                                    changedY = 0f
                                    dragOffsetPx = abs(dragOffsetPx)
                                    binding.ivImage.animate()
                                        .withStartAction {
                                            binding.blackScreen.animate().alpha(1f)
                                        }
                                        .withEndAction {
                                            view.parent.requestDisallowInterceptTouchEvent(false)
                                        }
                                        .translationY(centeredY)
                                }
                            }
                        }
                        MotionEvent.ACTION_DOWN -> {
                            dy = view.y - e.rawY
                            startY = view.y
                            changedY = startY
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (!singleTouchMode &&
                                (changedY !in (startY - dragOffsetPx)..(startY + dragOffsetPx))
                            ) {
                                if (changedY < startY - dragOffsetPx) dragOffsetPx = -dragOffsetPx
                                onImageDragStart()
                                singleTouchMode = true
                                view.parent.requestDisallowInterceptTouchEvent(true)
                            } else changedY = e.rawY + dy

                            if (singleTouchMode) {
                                view.y = e.rawY + dy - dragOffsetPx
                                val diff = if (startY > e.rawY + dy) {
                                    (startY - (e.rawY + dy)) / hideOffsetPx
                                } else {
                                    (e.rawY + dy - startY) / hideOffsetPx
                                }
                                binding.blackScreen.alpha = 1f - (if (diff > 1) 1f else diff) * 0.4f
                            }
                        }

                        MotionEvent.ACTION_POINTER_DOWN -> {
                            if (!singleTouchMode) {
                                setImageCenteredWithMatchParent()
                                multiTouchMode = true
                                return binding.ivImage.attacher.onTouch(view, e)
                            }
                        }

                    }
                    return true
                }
                return binding.ivImage.attacher.onTouch(view, e)
            }

            private fun isAClick(downX: Float, upX: Float, downY: Float, upY: Float): Boolean {
                val diffX = abs(downX - upX)
                val diffY = abs(downY - upY)
                return !(diffX > CLICK_ACTION_THRESHOLD || diffY > CLICK_ACTION_THRESHOLD)
            }
        }
    }
}