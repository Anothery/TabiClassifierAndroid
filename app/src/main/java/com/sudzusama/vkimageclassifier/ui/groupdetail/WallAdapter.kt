package com.sudzusama.vkimageclassifier.ui.groupdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.databinding.GroupWallItemBinding
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import java.text.SimpleDateFormat
import java.util.*

class WallAdapter(private val glide: RequestManager, private val context: Context) :
    RecyclerView.Adapter<WallAdapter.ViewHolder>() {
    private val posts = arrayListOf<WallItem>()

    fun setWall(newList: List<WallItem>) {
        val diffResult = DiffUtil.calculateDiff(WallDiffCallback(posts, newList))
        posts.clear()
        posts.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getWall() = posts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupWallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder(private val binding: GroupWallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var adapter: WallImageAdapter

        fun bind(wallItem: WallItem) {
            binding.tvDate.text =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(wallItem.date * 1000L)
            binding.rvWallItemImages.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            adapter = WallImageAdapter(glide)
            binding.rvWallItemImages.adapter = adapter
            adapter.setImages(wallItem.images)
        }
    }

}