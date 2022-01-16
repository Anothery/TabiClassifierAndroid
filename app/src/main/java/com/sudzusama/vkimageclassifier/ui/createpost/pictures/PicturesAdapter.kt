package com.sudzusama.vkimageclassifier.ui.createpost.pictures

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.CreatePostPictureItemBinding
import com.sudzusama.vkimageclassifier.domain.Genre
import com.sudzusama.vkimageclassifier.utils.view.gone
import com.sudzusama.vkimageclassifier.utils.view.invisible
import com.sudzusama.vkimageclassifier.utils.view.visible

class PicturesAdapter(
    private val glide: RequestManager,
    private val onRemoveClicked: (Picture) -> Unit,
    private val context: Context
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
        super.onViewRecycled(holder)
        holder.recycle()
    }

    inner class ViewHolder(private val binding: CreatePostPictureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture) {
            binding.dominantColorPalette.gone()
            binding.tvGenre.invisible()
            binding.fabRemove.gone()
            binding.fabRemove.setOnClickListener { onRemoveClicked(picture) }
            binding.ivPicture.requestLayout()
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
                            if (picture.isLoading) {
                                showClassifyingBackground()
                            } else {
                                hideClassifyingBackground()
                            }
                            picture.detail?.let { detail ->
                                binding.dominantColorPalette.scaleX = 0f
                                binding.dominantColorPalette.alpha = 0f
                                binding.dominantColorPalette.visible()
                                binding.dominantColorPalette.setColors(detail.colors)
                                binding.dominantColorPalette.updateLayoutParams {
                                    width = resource?.intrinsicWidth ?: 0
                                }
                                binding.dominantColorPalette.animate().alpha(1f).scaleX(1f)
                                binding.tvGenre.text = when (detail.genre) {
                                    is Genre.Frame -> context.getString(R.string.genre_anime_frame)
                                    is Genre.Manga -> context.getString(R.string.genre_manga)
                                    is Genre.Art -> context.getString(R.string.genre_art)
                                    else -> context.getString(R.string.genre_unknown)
                                }
                                binding.tvGenre.alpha = 0f
                                binding.tvGenre.visible()
                                binding.tvGenre.animate().alpha(1f).scaleX(1f).scaleY(1f)
                            }
                            binding.fabRemove.visible()
                            binding.flClassifying.updateLayoutParams {
                                width = resource?.intrinsicWidth ?: 0
                            }
                            return false
                        }

                    }).centerInside().into(binding.ivPicture)
            }
        }

        private fun showClassifyingBackground() {
            binding.flClassifying.alpha = 0f
            binding.flClassifying.visible()
            binding.flClassifying.animate().alpha(1f)
        }

        private fun hideClassifyingBackground() {
            binding.flClassifying.animate().alpha(0f).withEndAction { binding.flClassifying.gone() }
        }

        fun recycle() {
            glide.clear(binding.ivPicture)
        }

    }
}