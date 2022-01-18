package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallDeleteDesponse(
    @SerialName("response")
    val response: Int
)