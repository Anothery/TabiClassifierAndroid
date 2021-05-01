package com.sudzusama.vkimageclassifier.domain.model

data class GroupDetail(
    val description: String,
    val id: Int,
    val isAdmin: Int,
    val isAdvertiser: Int,
    val isClosed: Int,
    val isMember: Int,
    val name: String,
    val photo100: String,
    val photo200: String,
    val photo50: String,
    val screenName: String,
    val type: String
)