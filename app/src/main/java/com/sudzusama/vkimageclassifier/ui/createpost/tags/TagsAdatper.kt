package com.sudzusama.vkimageclassifier.ui.createpost.tags

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sudzusama.vkimageclassifier.databinding.TagItemBinding
import com.sudzusama.vkimageclassifier.utils.view.darken
import com.sudzusama.vkimageclassifier.utils.view.toPx

class TagsAdapter(
    private val onCheckedChange: (Boolean, Int) -> Unit
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private var tags: ArrayList<Tag> = arrayListOf()

    fun setTags(newList: List<Tag>) {
        val diffResult = DiffUtil.calculateDiff(TagsDiffCallback(tags, newList))
        tags.clear()
        tags.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tags[position], position)
    }

    override fun getItemCount(): Int = tags.size

    inner class ViewHolder(private val binding: TagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun isLightColor(color: Int): Boolean {
            val luminance = (0.299 * Color.red(color)
                    + 0.587 * Color.green(color)
                    + 0.114 * Color.blue(color)) / 255
            return luminance > 0.6
        }


        fun bind(tag: Tag, position: Int) {
            binding.chip.text = tag.name
            binding.chip.chipStrokeWidth = 2.toPx.toFloat()
            binding.chip.isChecked = tag.selected
            if (binding.chip.isChecked) {
                binding.chip.setTextColor(if (isLightColor(tag.color)) tag.color.darken else Color.WHITE)
                binding.chip.rippleColor = ColorStateList.valueOf(Color.WHITE)
                binding.chip.chipBackgroundColor = ColorStateList.valueOf(tag.color)
                binding.chip.chipStrokeWidth = 0f
            } else {
                binding.chip.setTextColor(if (isLightColor(tag.color)) tag.color.darken else tag.color)
                binding.chip.rippleColor = ColorStateList.valueOf(tag.color)
                binding.chip.chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
                binding.chip.chipStrokeWidth = 2.toPx.toFloat()
                binding.chip.chipStrokeColor =
                    ColorStateList.valueOf(if (isLightColor(tag.color)) tag.color.darken else tag.color)
            }
            
            binding.chip.setOnCheckedChangeListener { _, checked ->
                onCheckedChange(checked, position)
                if (checked) {
                    binding.chip.setTextColor(if (isLightColor(tag.color)) tag.color.darken else Color.WHITE)
                    binding.chip.rippleColor = ColorStateList.valueOf(Color.WHITE)
                    binding.chip.chipBackgroundColor = ColorStateList.valueOf(tag.color)
                    binding.chip.chipStrokeWidth = 0f
                } else {
                    binding.chip.setTextColor(if (isLightColor(tag.color)) tag.color.darken else tag.color)
                    binding.chip.rippleColor = ColorStateList.valueOf(tag.color)
                    binding.chip.chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
                    binding.chip.chipStrokeWidth = 2.toPx.toFloat()
                    binding.chip.chipStrokeColor =
                        ColorStateList.valueOf(if (isLightColor(tag.color)) tag.color.darken else tag.color)
                }
            }
        }
    }
}