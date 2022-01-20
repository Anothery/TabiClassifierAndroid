package com.sudzusama.vkimageclassifier.data.response.vk


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUploadServerResponse(
    @SerialName("album_id")
    val albumId: Int,
    @SerialName("upload_url")
    val uploadUrl: String,
    @SerialName("user_id")
    val userId: Long
)
