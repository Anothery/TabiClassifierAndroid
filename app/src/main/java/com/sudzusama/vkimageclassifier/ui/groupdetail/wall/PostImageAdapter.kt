package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sudzusama.vkimageclassifier.databinding.GroupWallImageBinding
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetail


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
        binding.root.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val density = parent.context.resources.displayMetrics.density
                val heightPx = (300 * density + 0.5f).toInt()
                val lp = binding.root.layoutParams
                if (lp is StaggeredGridLayoutManager.LayoutParams) {
                    when (viewType) {
                        VIEW_TYPE_FULL -> lp.apply {
                            isFullSpan = true
                            width = parent.width
                            height = heightPx
                        }
                        VIEW_TYPE_HALF -> lp.apply {
                            isFullSpan = false
                            width = parent.width / 2
                            height = heightPx
                        }
                    }
                    val lm = (parent as RecyclerView).layoutManager as StaggeredGridLayoutManager?
                    lm?.invalidateSpanAssignments()
                }
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }

        })
        return PostImageViewHolder(binding)
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

    inner class PostImageViewHolder(val binding: GroupWallImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: WallImageItem, position: Int) {
            binding.ivWallImage.setOnClickListener {
                val imagesList = mutableListOf<ImageDetail>()
                currentRecyclerView?.let { rv ->
                    for (i in 0 until itemCount) {
                        val holder = rv.getChildViewHolder(rv.getChildAt(i)) as PostImageViewHolder
                        with(holder.binding.ivWallImage) {
                            val xy = IntArray(2).apply { getLocationOnScreen(this) }
                            imagesList.add(ImageDetail(images[i].url, width, height, xy[0], xy[1]))
                        }
                    }
                }

                onImageClicked(imagesList, position)
            }

            binding.root.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (images.size == 1) {
                        val ratio = 1f * image.height / image.width
                        binding.root.layoutParams = binding.root.layoutParams.apply {
                            height = (width * ratio).toInt()
                        }
                    }
                    binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            })

            glide.load(image.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .apply {
                    if (getItemViewType(position) == VIEW_TYPE_FULL) centerInside().into(binding.ivWallImage)
                    else centerCrop().into(binding.ivWallImage)
                }
        }
    }
}