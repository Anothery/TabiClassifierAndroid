package com.sudzusama.vkimageclassifier.data.response.vk


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    @SerialName("can_access_closed")
    val canAccessClosed: Boolean,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("id")
    val id: Int,
    @SerialName("is_closed")
    val isClosed: Boolean,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("photo_200")
    val photo200: String
)
