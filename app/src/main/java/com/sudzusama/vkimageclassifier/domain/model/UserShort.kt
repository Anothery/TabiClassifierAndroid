package com.sudzusama.vkimageclassifier.domain.model

data class UserShort(
    val canAccessClosed: Boolean,
    val firstName: String,
    val id: Int,
    val isClosed: Boolean,
    val lastName: String,
    val avatarMedium: String
)