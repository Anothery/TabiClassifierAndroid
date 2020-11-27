package com.sudzusama.vkimageclassifier.ui.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sudzusama.vkimageclassifier.databinding.GroupItemBinding
import com.sudzusama.vkimageclassifier.domain.model.Group

class GroupsAdapter(
    private val context: Context, private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    private var groups: List<Group> = listOf()

    fun setGroups(list: List<Group>) {
        groups = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.ViewHolder {
        val binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.ViewHolder, position: Int) =
        holder.bind(groups[position])

    override fun getItemCount(): Int = groups.size

    inner class ViewHolder(private val binding: GroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.root.setOnClickListener { onItemClicked(group.id) }
            binding.tvGroupName.text = group.name
            binding.tvGroupType.text = group.activity
            Glide.with(context).load(group.photo200).into(binding.ivGroupAvatar)
        }
    }

}