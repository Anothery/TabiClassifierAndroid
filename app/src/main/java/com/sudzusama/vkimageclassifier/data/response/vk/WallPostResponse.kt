package com.sudzusama.vkimageclassifier.data.response.vk


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallPostResponse(
    @SerialName("post_id")
    val postId: Int
)
