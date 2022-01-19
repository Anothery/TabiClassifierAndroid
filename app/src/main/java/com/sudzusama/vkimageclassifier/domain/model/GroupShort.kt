package com.sudzusama.vkimageclassifier.domain.model

data class GroupShort(
    val id: Int,
    val name: String,
    val type: Int,
    val adminLevel: Int?,
    val isAdmin: Boolean,
    val isAdvertiser: Int,
    val isClosed: Int,
    val isMember: Int,
    val photo100: String,
    val photo200: String,
    val photo50: String,
    val screenName: String?,
    val activity: String?,
    val canPost: Boolean
)