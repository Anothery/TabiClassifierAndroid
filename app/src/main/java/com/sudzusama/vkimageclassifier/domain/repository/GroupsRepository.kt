package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem

interface GroupsRepository {
    suspend fun getGroups(
        version: String,
        userId: Long,
        extended: Int,
        fields: List<String>
    ): List<GroupShort>

    suspend fun getGroupById(
        version: String,
        groupId: Long,
        fields: List<String>
    ): GroupDetail

    suspend fun getWallById(version: String, groupId: Long, offset: Int, count: Int): List<WallItem>
}
