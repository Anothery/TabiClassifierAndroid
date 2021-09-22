package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.GroupWallImageBinding
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem


class WallImageAdapter(private val glide: RequestManager) :
    RecyclerView.Adapter<WallImageAdapter.ViewHolder>() {

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

        binding.root.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val lp: ViewGroup.LayoutParams = binding.root.layoutParams
                if (lp is StaggeredGridLayoutManager.LayoutParams) {
                    val sglp = lp
                    when (viewType) {
                        VIEW_TYPE_FULL -> sglp.apply {
                            width = parent.width
                            height = 600
                            isFullSpan = true
                        }
                        VIEW_TYPE_HALF -> sglp.apply {
                            isFullSpan = false
                            width = parent.width / 2
                            height = 500
                        }
                    }
                    binding.root.layoutParams = sglp
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
        holder.bind(images[position], position)
    }

    inner class ViewHolder(private val binding: GroupWallImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: WallImageItem, position: Int) {
            glide.load(image.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.group_stub_avatar)
                .into(binding.ivWallImage)
        }
    }
}