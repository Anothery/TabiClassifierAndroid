package com.sudzusama.vkimageclassifier.ui.createpost

import androidx.recyclerview.widget.DiffUtil

class PicturesDiffCallback(private val oldList: List<Picture>, private val newList: List<Picture>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].uri == newList[newItemPosition].uri


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.hashCode() == newItem.hashCode()
    }
}