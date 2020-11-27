package com.sudzusama.vkimageclassifier.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupsListResponse(
    @SerialName("response") val response: Response
)

@Serializable
data class Response(
    @SerialName("count") val count: Int? = null,
    @SerialName("items") val groups: List<Group>
)

@Serializable
data class Group(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("screen_name") val screenName: String? = null,
    @SerialName("is_closed") val isClosed: Int,
    @SerialName("type") val type: String,
    @SerialName("is_admin") val isAdmin: Int,
    @SerialName("admin_level") val adminLevel: Int? = null,
    @SerialName("is_member") val isMember: Int,
    @SerialName("is_advertiser") val isAdvertiser: Int,
    @SerialName("photo_50") val photo50: String,
    @SerialName("photo_100") val photo100: String,
    @SerialName("photo_200") val photo200: String,
    @SerialName("activity") val activity: String?
)
