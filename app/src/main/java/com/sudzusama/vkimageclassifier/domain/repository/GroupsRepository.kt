package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture

interface GroupsRepository {
    suspend fun getGroups(
        userId: Long,
        extended: Int,
        filter: String?,
        fields: List<String>
    ): List<GroupShort>

    suspend fun getGroupById(
        groupId: Int,
        fields: List<String>
    ): GroupDetail

    suspend fun likeAnItem(ownerId: Int, itemId: Int, type: String)
    suspend fun removeLikeFromItem(ownerId: Int, itemId: Int, type: String)
    suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem>

    suspend fun uploadPhotos(
        groupId: Int,
        photos: List<Picture>
    ): List<String>

    suspend fun postToWall(
        ownerId: Int,
        fromGroup: Int,
        message: String?,
        attachements: List<String>?,
        publishDate: Long?
    ): Int

    suspend fun deletePost(groupId: Int, postId: Int): Boolean
}

