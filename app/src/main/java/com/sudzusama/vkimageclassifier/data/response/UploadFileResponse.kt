package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponse(
    @SerialName("hash")
    val hash: String,
    @SerialName("photo")
    val photo: String,
    @SerialName("server")
    val server: Int
)