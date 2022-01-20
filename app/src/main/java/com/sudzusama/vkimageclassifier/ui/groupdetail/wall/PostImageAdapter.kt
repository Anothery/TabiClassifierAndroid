package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sudzusama.vkimageclassifier.databinding.GroupWallImageBinding
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetail
import com.sudzusama.vkimageclassifier.utils.view.toPx


class PostImageAdapter(
    private val glide: RequestManager,
    private val onImageClicked: (List<ImageDetail>, Int) -> Unit
) : RecyclerView.Adapter<PostImageAdapter.PostImageViewHolder>() {

    private var currentRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        currentRecyclerView = recyclerView
    }

    companion object {
        const val VIEW_TYPE_FULL = 0
        const val VIEW_TYPE_HALF = 1
    }

    private val images = arrayListOf<WallImageItem>()

    fun setImages(newList: List<WallImageItem>) {
        val diffResult = DiffUtil.calculateDiff(WallImageDiffCallback(images, newList))
        images.clear()
        images.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImageViewHolder {
        val binding =
            GroupWallImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostImageViewHolder(binding, parent.measuredWidth)
    }

    override fun getItemViewType(position: Int): Int {
        if (images.size == 2) return VIEW_TYPE_HALF
        if (position == images.size - 1 && position % 3 == 1) return VIEW_TYPE_FULL

        return when (position % 3) {
            0 -> VIEW_TYPE_FULL
            else -> VIEW_TYPE_HALF
        }
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: PostImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun onViewRecycled(holder: PostImageViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    inner class PostImageViewHolder(
        private val binding: GroupWallImageBinding,
        private val parentWidth: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: WallImageItem, position: Int) {
            val ratio = 1f * image.height / image.width
            when (getItemViewType(position)) {
                VIEW_TYPE_FULL -> binding.root.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                    isFullSpan = true
                    width = parentWidth
                    height = (width * ratio).toInt()

                }
                VIEW_TYPE_HALF -> binding.root.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                    isFullSpan = false
                    height = 300.toPx
                }
            }

            glide.load(image.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade(500))
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
                        binding.ivWallImage.setOnClickListener {
                            val imagesList = mutableListOf<ImageDetail>()
                            currentRecyclerView?.let { rv ->
                                for (i in 0 until itemCount) {
                                    val holder =
                                        rv.getChildViewHolder(rv.getChildAt(i)) as PostImageViewHolder
                                    with(holder.binding.ivWallImage) {
                                        val xy = IntArray(2).apply { getLocationOnScreen(this) }
                                        imagesList.add(
                                            ImageDetail(
                                                images[i].url,
                                                width,
                                                height,
                                                xy[0],
                                                xy[1]
                                            )
                                        )
                                    }
                                }
                            }
                            onImageClicked(imagesList, position)
                        }
                        return false
                    }

                })
                .into(binding.ivWallImage)
        }

        fun recycle() {
            binding.ivWallImage.apply { glide.clear(this) }
        }
    }
}