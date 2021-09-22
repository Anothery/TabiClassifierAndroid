package com.sudzusama.vkimageclassifier.domain.model

data class WallItem(val id: Int, val date: Int, val images: List<WallImageItem>)

data class WallImageItem(
    val id: Int,
    val height: Int,
    val width: Int,
    val url: String
)