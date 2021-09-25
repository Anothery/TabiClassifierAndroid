package com.sudzusama.vkimageclassifier.data.repository

import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import javax.inject.Inject

class VkGroupsRepository @Inject constructor(
    private val groupsApi: GroupsApi
) : GroupsRepository {
    override suspend fun getGroups(
        version: String,
        userId: Int,
        extended: Int,
        filter: String?,
        fields: List<String>
    ): List<GroupShort> {
        val groups =
            groupsApi.getGroups(version, userId, extended, filter, fields.joinToString(","))
        return groups.mapToDomain()
    }

    override suspend fun getGroupById(
        version: String,
        groupId: Int,
        fields: List<String>
    ): GroupDetail {
        val group = groupsApi.getGroupById(version, groupId, fields.joinToString(","))
        return group.mapToDomain()
    }

    override suspend fun getWallById(
        version: String,
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem> {
        val wall = groupsApi.getWallById(
            version,
            groupId,
            offset,
            count,
            extended,
            fields?.joinToString(",")
        )
        return wall.mapToDomain()
    }

    override suspend fun likeAnItem(
        version: String,
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsApi.likeAnItem(version, ownerId, itemId, type)
    }


    override suspend fun removeLikeFromItem(
        version: String,
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsApi.removeLikeFromItem(version, ownerId, itemId, type)
    }
}