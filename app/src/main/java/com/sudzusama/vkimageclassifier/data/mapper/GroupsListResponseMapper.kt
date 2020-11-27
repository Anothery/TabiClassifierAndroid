package com.sudzusama.vkimageclassifier.data.mapper

import com.sudzusama.vkimageclassifier.data.response.GroupsListResponse
import com.sudzusama.vkimageclassifier.domain.model.Group

class GroupsListResponseMapper {
    fun map(groupsListResponse: GroupsListResponse): List<Group> =
        groupsListResponse.response.groups.map {
            Group(
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
}