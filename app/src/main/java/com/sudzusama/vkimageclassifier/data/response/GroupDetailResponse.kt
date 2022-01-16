package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDetailResponse(
    @SerialName("response")
    val response: List<Response> = listOf()
) {
    @Serializable
    data class Response(
        @SerialName("id")
        val id: Int = 0,
        @SerialName("is_admin")
        val isAdmin: Int = 0,
        @SerialName("is_advertiser")
        val isAdvertiser: Int = 0,
        @SerialName("is_closed")
        val isClosed: Int = 0,
        @SerialName("is_member")
        val isMember: Int = 0,
        @SerialName("can_post")
        val canPost: Int = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("photo_100")
        val photo100: String = "",
        @SerialName("photo_200")
        val photo200: String = "",
        @SerialName("photo_50")
        val photo50: String = "",
        @SerialName("screen_name")
        val screenName: String = "",
        @SerialName("type")
        val type: String = ""
    )
}