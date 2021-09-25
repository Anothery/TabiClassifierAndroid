package com.sudzusama.vkimageclassifier.data.mapper

import com.sudzusama.vkimageclassifier.data.response.GroupDetailResponse
import com.sudzusama.vkimageclassifier.data.response.GroupWallResponse
import com.sudzusama.vkimageclassifier.data.response.GroupsListResponse
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallImageItem
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import kotlin.math.abs

fun GroupsListResponse.mapToDomain(): List<GroupShort> = this.response.groups.map {
    GroupShort(
        it.id,
        it.name,
        it.type,
        it.adminLevel,
        it.isAdmin,
        it.isAdvertiser,
        it.isClosed,
        it.isMember,
        it.photo100,
        it.photo200,
        it.photo50,
        it.screenName,
        it.activity
    )
}

fun GroupDetailResponse.mapToDomain(): GroupDetail = with(this.response[0]) {
    return@with GroupDetail(
        this.id,
        this.name,
        this.photo50,
        this.photo100,
        this.photo200
    )
}

fun GroupWallResponse.mapToDomain(): List<WallItem> =
    this.response.items.map { response ->
        var posterName = ""
        var posterThumbnail = ""


        this.response.groups.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
            posterName = it.name
            posterThumbnail = it.photo50
        } ?: this.response.profiles.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)
            ?.let {
                posterName = "${it.firstName} ${it.lastName}"
                posterThumbnail = it.photo50
            }
        WallItem(
            response.id,
            posterName,
            posterThumbnail,
            response.date,
            response.likes.userLikes,
            response.likes.count,
            response.attachments.filter { it.type == "photo" }
                .map {
                    WallImageItem(
                        it.photo.id,
                        it.photo.sizes.filter { resized -> resized.type == "r" }[0].height,
                        it.photo.sizes.filter { resized -> resized.type == "r" }[0].width,
                        it.photo.sizes.filter { resized -> resized.type == "r" }[0].url
                    )
                })
    }



