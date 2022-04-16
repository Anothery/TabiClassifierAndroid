package com.sudzusama.vkimageclassifier.data.mapper

import com.sudzusama.vkimageclassifier.data.response.tabi.TabiClassifyResponse
import com.sudzusama.vkimageclassifier.data.response.vk.GetUserResponse
import com.sudzusama.vkimageclassifier.data.response.vk.GroupDetailResponse
import com.sudzusama.vkimageclassifier.data.response.vk.GroupWallResponse
import com.sudzusama.vkimageclassifier.data.response.vk.GroupsListResponse
import com.sudzusama.vkimageclassifier.domain.model.*
import kotlin.math.abs

fun GetUserResponse.mapToDomain(): UserShort =
    UserShort(canAccessClosed, firstName, id, isClosed, lastName, photo200)

fun GroupsListResponse.mapToDomain(): List<GroupShort> = this.groups.map {
    GroupShort(
        it.id,
        it.name,
        when (it.type) {
            "event" -> GroupTypes.EVENT
            "page" -> GroupTypes.PAGE
            else -> GroupTypes.GROUP
        },
        it.adminLevel,
        it.isAdmin != 0,
        it.isAdvertiser,
        it.isClosed,
        it.isMember,
        it.photo100,
        it.photo200,
        it.photo50,
        it.screenName,
        it.activity,
        it.canPost != null && it.canPost != 0
    )
}

fun TabiClassifyResponse.mapToDomain(): ClassifyResponse =
    ClassifyResponse(
        colors.map { ClassifyResponse.Color(it.meanHexColor, it.name, it.percentage.toFloat()) },
        ClassifyResponse.Predictions(
            this.predictions.art,
            this.predictions.frame,
            this.predictions.manga
        )
    )

fun List<GroupDetailResponse>.mapToDomain(): GroupDetail = with(this[0]) {
    return@with GroupDetail(
        this.id,
        this.name,
        this.photo50,
        this.photo100,
        this.photo200,
        this.isAdmin != 0,
        this.canPost != 0,
        when (type) {
            "event" -> GroupTypes.EVENT
            "page" -> GroupTypes.PAGE
            else -> GroupTypes.GROUP
        }
    )
}

fun GroupWallResponse.mapToDomain(): List<WallItem> =
    this.items.map { response ->
        var posterName = ""
        var posterThumbnail = ""


        this.groups.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
            it.photo50
            posterName = it.name
            posterThumbnail = it.photo50
        } ?: this.profiles.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)
            ?.let {
                posterName = "${it.firstName} ${it.lastName}"
                posterThumbnail = it.photo50
            }
        WallItem(
            response.id,
            response.text,
            posterName,
            posterThumbnail,
            response.date,
            response.likes.userLikes,
            response.likes.count,
            response.attachments.filter { it.type == "photo" }
                .map {
                    WallImageItem(
                        it.photo.id,
                        it.photo.sizes.filter { resized -> resized.type == "r" }
                            .getOrNull(0)?.height ?: 0,
                        it.photo.sizes.filter { resized -> resized.type == "r" }.getOrNull(0)?.width
                            ?: 0,
                        it.photo.sizes.filter { resized -> resized.type == "r" }.getOrNull(0)?.url
                            ?: ""
                    )
                })
    }



