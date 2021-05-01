package com.sudzusama.vkimageclassifier.domain.model

data class GroupShort(
    val id: Long,
    val name: String,
    val type: String,
    val adminLevel: Int?,
    val isAdmin: Int,
    val isAdvertiser: Int,
    val isClosed: Int,
    val isMember: Int,
    val photo100: String,
    val photo200: String,
    val photo50: String,
    val screenName: String?,
    val activity: String?,
)