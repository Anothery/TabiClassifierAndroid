package com.sudzusama.vkimageclassifier.data.mapper

import com.sudzusama.vkimageclassifier.data.response.GroupDetailResponse
import com.sudzusama.vkimageclassifier.data.response.GroupsListResponse
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort

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
        this.description,
        this.id,
        this.isAdmin,
        this.isAdvertiser,
        this.isClosed,
        this.isMember,
        this.name,
        this.photo100,
        this.photo200,
        this.photo50,
        this.screenName,
        this.type
    )
}
