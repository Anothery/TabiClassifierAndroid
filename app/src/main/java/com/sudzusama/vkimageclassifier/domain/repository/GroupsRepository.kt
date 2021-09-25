package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem

interface GroupsRepository {
    suspend fun getGroups(
        version: String,
        userId: Int,
        extended: Int,
        filter: String?,
        fields: List<String>
    ): List<GroupShort>

    suspend fun getGroupById(
        version: String,
        groupId: Int,
        fields: List<String>
    ): GroupDetail

    suspend fun likeAnItem(version: String, ownerId: Int, itemId: Int, type: String)
    suspend fun removeLikeFromItem(version: String, ownerId: Int, itemId: Int, type: String)
    suspend fun getWallById(
        version: String,
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem>
}
