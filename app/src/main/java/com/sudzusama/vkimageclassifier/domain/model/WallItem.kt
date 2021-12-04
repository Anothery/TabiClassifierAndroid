package com.sudzusama.vkimageclassifier.domain.model

data class WallItem(
    val id: Int,
    val text: String,
    val posterName: String,
    val posterThumbnail: String,
    val date: Int,
    var userLikes: Int,
    var likesCount: Int,
    val images: List<WallImageItem>
)

data class WallImageItem(
    val id: Int,
    val height: Int,
    val width: Int,
    val url: String,
)