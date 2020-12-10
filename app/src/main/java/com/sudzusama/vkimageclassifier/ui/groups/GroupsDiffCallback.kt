package com.sudzusama.vkimageclassifier.ui.groups

import androidx.recyclerview.widget.DiffUtil
import com.sudzusama.vkimageclassifier.domain.model.Group

class GroupsDiffCallback(private val oldList: List<Group>, private val newList: List<Group>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.hashCode() == newItem.hashCode()
    }
}