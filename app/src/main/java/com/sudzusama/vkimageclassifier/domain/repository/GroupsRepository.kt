package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.data.response.SaveWallResponse
import com.sudzusama.vkimageclassifier.data.response.WallPostResponse
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture

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

    suspend fun uploadPhotos(
        version: String,
        groupId: Int,
        photos: List<Picture>
    ): List<String>

    suspend fun postToWall(version: String, ownerId: Int, fromGroup: Int, message: String?, attachements: List<String>?): Int
}

