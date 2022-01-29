package com.sudzusama.vkimageclassifier.ui.groupdetail.wall

import android.text.SpannableStringBuilder
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem

data class WallItemFormatted(
    val id: Int,
    val text: SpannableStringBuilder,
    val posterName: String,
    val posterThumbnail: String,
    val date: Int,
    var userLikes: Int,
    var likesCount: Int,
    val images: List<WallImageItem>
)
