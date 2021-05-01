package com.sudzusama.vkimageclassifier.data.repository

import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import javax.inject.Inject

class VkGroupsRepository @Inject constructor(
    private val groupsApi: GroupsApi
) : GroupsRepository {
    override suspend fun getGroups(
        version: String,
        userId: Long,
        extended: Int,
        fields: List<String>
    ): List<GroupShort> {
        val groups = groupsApi.getGroups(version, userId, extended, fields.joinToString(","))
        return groups.mapToDomain()
    }

    override suspend fun getGroupById(
        version: String,
        groupId: Long,
        fields: List<String>
    ): GroupDetail {
        val group = groupsApi.getGroupById(version, groupId, fields.joinToString(","))
        return group.mapToDomain()
    }
}