package com.sudzusama.vkimageclassifier.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDetailResponse(
    @SerialName("response")
    val response: List<Response>
) {
    @Serializable
    data class Response(
        @SerialName("can_post")
        val canPost: Int,
        @SerialName("description")
        val description: String,
        @SerialName("id")
        val id: Int,
        @SerialName("is_admin")
        val isAdmin: Int,
        @SerialName("is_advertiser")
        val isAdvertiser: Int,
        @SerialName("is_closed")
        val isClosed: Int,
        @SerialName("is_member")
        val isMember: Int,
        @SerialName("members_count")
        val membersCount: Int,
        @SerialName("name")
        val name: String,
        @SerialName("photo_100")
        val photo100: String,
        @SerialName("photo_200")
        val photo200: String,
        @SerialName("photo_50")
        val photo50: String,
        @SerialName("screen_name")
        val screenName: String,
        @SerialName("type")
        val type: String
    )
}