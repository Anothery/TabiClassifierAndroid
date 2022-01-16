package com.sudzusama.vkimageclassifier.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupDetail(
    val id: Int,
    val name: String,
    val photo50: String,
    val photo100: String,
    val photo200: String,
    val isAdmin: Boolean,
    val canPost: Boolean,
    val type: Int,
) : Parcelable

object GroupTypes {
    const val GROUP = 0
    const val PAGE = 1
    const val EVENT = 2
}