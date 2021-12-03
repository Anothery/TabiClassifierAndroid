package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.GroupWallItemBinding
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.ui.imagedetail.ImageDetail
import java.text.SimpleDateFormat
import java.util.*

class WallAdapter(
    private val glide: RequestManager,
    private val onPostLiked: (Int, Boolean) -> Unit,
    private val onImageClicked: (List<ImageDetail>, Int) -> Unit) :
    RecyclerView.Adapter<WallAdapter.WallItemViewHolder>() {
    private val posts = arrayListOf<WallItem>()

    fun setWall(newList: List<WallItem>) {
        val diffResult = DiffUtil.calculateDiff(WallDiffCallback(posts, newList))
        posts.clear()
        posts.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getWall() = posts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallItemViewHolder {
        val binding =
            GroupWallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.rvWallItemImages.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvWallItemImages.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val density = parent.context.resources.displayMetrics.density
                val position = parent.getChildAdapterPosition(view)

                if (state.itemCount == 2) {
                    when (position) {
                        0 -> outRect.right = (2 * density).toInt()
                        1 -> outRect.left = (2 * density).toInt()
                    }
                    return
                }

                if (position % 3 in 0..1 && position == state.itemCount - 1) return

                outRect.bottom = (4 * density).toInt()

                when (position % 3) {
                    1 -> outRect.right = (2 * density).toInt()
                    2 -> outRect.left = (2 * density).toInt()
                }
            }
        })
        val adapter = PostImageAdapter(glide, onImageClicked)
        binding.rvWallItemImages.adapter = adapter
        return WallItemViewHolder(binding, adapter)

    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: WallItemViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class WallItemViewHolder(
        private val binding: GroupWallItemBinding,
        private val adapter: PostImageAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(wallItem: WallItem) {
            binding.tvDate.text =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(wallItem.date * 1000L)
            binding.tvLikeCount.text = "${if (wallItem.likesCount > 0) wallItem.likesCount else ""}"
            binding.tvPosterName.text = wallItem.posterName
            glide.load(wallItem.posterThumbnail)
                .error(R.drawable.group_stub_avatar)
                .circleCrop()
                .into(binding.ivPosterAvatar)

            if (wallItem.userLikes > 0) {
                binding.ivLike.setImageResource(R.drawable.outline_favorite_24)
            }

            binding.ivLike.setOnClickListener {
                val isLiked = wallItem.userLikes > 0
                onPostLiked(wallItem.id, isLiked)
                if (wallItem.userLikes > 0) {
                    wallItem.likesCount--
                    wallItem.userLikes = 0
                    binding.ivLike.setImageResource(R.drawable.outline_favorite_border_24)
                } else {
                    wallItem.likesCount++
                    wallItem.userLikes++
                    binding.ivLike.setImageResource(R.drawable.outline_favorite_24)
                }
                binding.tvLikeCount.text = "${wallItem.likesCount}"
            }

            adapter.setImages(wallItem.images)
        }
    }

}