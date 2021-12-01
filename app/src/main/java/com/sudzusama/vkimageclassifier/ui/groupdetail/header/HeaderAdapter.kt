package com.sudzusama.vkimageclassifier.ui.groupdetail.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.databinding.GroupDetailInfoBinding
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail

class HeaderAdapter(
    private val glide: RequestManager,
) : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    private val headers = arrayListOf<GroupDetail>()

    fun setHeaders(newList: List<GroupDetail>) {
        headers.clear()
        headers.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = GroupDetailInfoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderViewHolder(binding)

    }

    override fun getItemCount() = headers.size


    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(headers[position])
    }

    inner class HeaderViewHolder(private val binding: GroupDetailInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: GroupDetail) {
            binding.tvGroupName.text = header.name
            glide.load(header.photo200)
                .error(R.drawable.group_stub_avatar)
                .circleCrop()
                .into(binding.ivGroupAvatar)
        }
    }

}