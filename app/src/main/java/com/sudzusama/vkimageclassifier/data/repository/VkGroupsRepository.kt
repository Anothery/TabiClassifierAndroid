package com.sudzusama.vkimageclassifier.data.repository

import com.sudzusama.vkimageclassifier.data.mapper.GroupsListResponseMapper
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.domain.model.Group
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import javax.inject.Inject

class VkGroupsRepository @Inject constructor(
    private val groupsApi: GroupsApi,
    private val groupsListResponseMapper: GroupsListResponseMapper

) : GroupsRepository {
    override suspend fun getGroups(
        version: String,
        userId: String,
        extended: Int,
        fields: List<String>
    ): List<Group> {
        val groups = groupsApi.getGroups(version, userId, extended, fields.joinToString(","))
        return groupsListResponseMapper.map(groups)
    }
}