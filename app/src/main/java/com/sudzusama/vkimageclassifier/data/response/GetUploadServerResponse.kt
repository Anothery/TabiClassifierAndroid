package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUploadServerResponse(
    @SerialName("response")
    val response: Response
) {
    @Serializable
    data class Response(
        @SerialName("album_id")
        val albumId: Int,
        @SerialName("upload_url")
        val uploadUrl: String,
        @SerialName("user_id")
        val userId: Long
    )
}