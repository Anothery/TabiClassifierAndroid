package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import androidx.recyclerview.widget.DiffUtil
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem

class WallImageDiffCallback(
    private val oldList: List<WallImageItem>,
    private val newList: List<WallImageItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}