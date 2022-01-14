package com.sudzusama.vkimageclassifier.ui.createpost.pictures

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sudzusama.vkimageclassifier.databinding.CreatePostPictureItemBinding
import com.sudzusama.vkimageclassifier.utils.view.dominantcolor.DominantColor

class PicturesAdapter(
    private val glide: RequestManager,
    private val onRemoveClicked: (Picture) -> Unit
) :
    RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    private var pictures: ArrayList<Picture> = arrayListOf()

    fun setPictures(newList: List<Picture>) {
        val diffResult = DiffUtil.calculateDiff(PicturesDiffCallback(pictures, newList))
        pictures.clear()
        pictures.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CreatePostPictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    override fun getItemCount(): Int = pictures.size

    override fun onViewRecycled(holder: ViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    inner class ViewHolder(val binding: CreatePostPictureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture) {
            binding.fabRemove.animate().alpha(0f)
            binding.fabRemove.setOnClickListener { onRemoveClicked(picture) }
            binding.ivPicture.doOnNextLayout {
                glide.load(picture.uri)
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
                            binding.fabRemove.animate().alpha(1f)
                            binding.dominantColorPalette.updateLayoutParams {
                                width = resource?.intrinsicWidth ?: 0
                            }
                            return false
                        }

                    }).centerInside().into(binding.ivPicture)
            }
        }

        fun recycle() {
            binding.ivPicture.apply { glide.clear(this) }
        }
    }
}