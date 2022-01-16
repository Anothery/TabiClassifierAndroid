package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallPostResponse(
    @SerialName("response")
    val response: Response
) {
    @Serializable
    data class Response(
        @SerialName("post_id")
        val postId: Int
    )
}