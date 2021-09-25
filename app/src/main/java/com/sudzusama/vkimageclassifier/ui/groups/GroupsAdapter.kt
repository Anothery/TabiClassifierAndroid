package com.sudzusama.vkimageclassifier.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.GroupItemBinding
import com.sudzusama.vkimageclassifier.domain.model.GroupShort

class GroupsAdapter(
    private val glide: RequestManager,
    private val onItemClicked: (Int) -> Unit
) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    private var groupShorts: ArrayList<GroupShort> = arrayListOf()

    fun setGroups(newList: List<GroupShort>) {
        val diffResult = DiffUtil.calculateDiff(GroupsDiffCallback(groupShorts, newList))
        groupShorts.clear()
        groupShorts.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.ViewHolder {
        val binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.ViewHolder, position: Int) =
        holder.bind(groupShorts[position])

    override fun getItemCount(): Int = groupShorts.size

    override fun onViewRecycled(holder: ViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    inner class ViewHolder(private val binding: GroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(groupShort: GroupShort) {
            binding.root.setOnClickListener { onItemClicked(groupShort.id) }
            binding.tvGroupName.text = groupShort.name
            binding.tvGroupType.text = groupShort.activity

            if (groupShort.isAdmin != 0) {
                binding.ivAdminPrivilege.visibility = View.VISIBLE
            } else {
                binding.ivAdminPrivilege.visibility = View.INVISIBLE
            }

            binding.ivGroupAvatar.apply {
                glide
                    .load(groupShort.photo200)
                    .error(R.drawable.group_stub_avatar)
                    .circleCrop()
                    .into(this)
            }
        }

        fun recycle() {
            binding.ivGroupAvatar.apply { glide.clear(this) }
        }
    }
}