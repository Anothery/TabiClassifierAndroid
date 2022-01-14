package com.sudzusama.vkimageclassifier.ui.createpost.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.databinding.GalleryItemBinding

class GalleryAdapter(
    private val galleryItems: ArrayList<GalleryItem>,
    private val glide: RequestManager,
    private val onItemClicked: (GalleryItem, Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(galleryItems[position], position)

    override fun getItemCount(): Int = galleryItems.size

    fun selectItem(position: Int) {
        galleryItems[position] = GalleryItem(galleryItems[position].uri, true)
        notifyItemChanged(position)
    }

    fun deselectItem(position: Int) {
        galleryItems[position] = GalleryItem(galleryItems[position].uri, false)
        notifyItemChanged(position)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    inner class ViewHolder(private val binding: GalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(galleryItem: GalleryItem, position: Int) {
            if (galleryItem.selected) {
                binding.flSelected.animate().alpha(1f)
                binding.ivSelectedDone.scaleX = 0f
                binding.ivSelectedDone.scaleY = 0f
                binding.ivSelectedDone.animate().scaleX(1f).scaleY(1f)
            } else {
                binding.flSelected.animate().alpha(0f)
                binding.ivSelectedDone.animate().scaleX(0f).scaleY(0f)
            }
            glide.load(galleryItem.uri).centerCrop().into(binding.ivPicture)
            binding.ivPicture.setOnClickListener { onItemClicked(galleryItem, position) }

        }

        fun recycle() {
            binding.ivPicture.apply { glide.clear(this) }
        }
    }
}