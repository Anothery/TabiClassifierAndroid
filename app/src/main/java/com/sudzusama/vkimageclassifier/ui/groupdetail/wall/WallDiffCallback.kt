package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import androidx.recyclerview.widget.DiffUtil
import com.sudzusama.vkimageclassifier.domain.model.WallItem

class WallDiffCallback(
    private val oldList: List<WallItem?>,
    private val newList: List<WallItem?>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition]?.id == newList[newItemPosition]?.id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.hashCode() == newItem.hashCode()
    }
}