package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sudzusama.vkimageclassifier.databinding.GroupWallImageBinding
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem


class PostImageAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<PostImageAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        return ViewHolder(binding)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    inner class ViewHolder(private val binding: GroupWallImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: WallImageItem) {
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
                .into(binding.ivWallImage)
        }
    }
}