package com.sudzusama.vkimageclassifier.ui.createpost.tags

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
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
        holder.bind(tags[position])
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

        fun bind(tag: Tag) {
            binding.chip.text = tag.name
            setChipChecked(tag.selected, tag.color, binding.chip)
            binding.chip.setOnCheckedChangeListener { _, _ ->
                onCheckedChange(!tag.selected, tags.indexOf(tag))
            }
        }

        private fun setChipChecked(checked: Boolean, color: Int, chip: Chip) {
            if (checked) {
                chip.setTextColor(if (isLightColor(color)) color.darken else Color.WHITE)
                chip.rippleColor = ColorStateList.valueOf(Color.WHITE)
                chip.chipBackgroundColor = ColorStateList.valueOf(color)
                chip.chipStrokeWidth = 0f
            } else {
                chip.setTextColor(if (isLightColor(color)) color.darken else color)
                chip.rippleColor = ColorStateList.valueOf(color)
                chip.chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
                chip.chipStrokeWidth = 2.toPx.toFloat()
                chip.chipStrokeColor =
                    ColorStateList.valueOf(if (isLightColor(color)) color.darken else color)
            }
        }
    }
}