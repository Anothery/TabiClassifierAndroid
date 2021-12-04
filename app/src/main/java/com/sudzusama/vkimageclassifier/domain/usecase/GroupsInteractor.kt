package com.sudzusama.vkimageclassifier.domain.usecase

import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import javax.inject.Inject

class GroupsInteractor @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val groupsRepository: GroupsRepository
) {
    companion object {
        private const val API_VERSION = "5.131"
        private const val EXTENDED = 1
        const val LIKE_TYPE_POST = "post"
    }

    suspend fun getGroups(): List<GroupShort> {
        val fields = listOf("activity")
        val filter = "admin,moder,editor"
        val nonModGroups = groupsRepository.getGroups(
            API_VERSION,
            authInteractor.getUserId(),
            EXTENDED,
            null,
            fields
        )
        val modGroups = groupsRepository.getGroups(
            API_VERSION,
            authInteractor.getUserId(),
            EXTENDED,
            filter,
            fields
        )
        return arrayListOf<GroupShort>().apply { addAll(modGroups); addAll(nonModGroups) }.distinctBy { it.id }
    }

    suspend fun getGroupById(id: Int): GroupDetail {
        val fields = listOf("description", "can_post", "ban_info", "members_count")
        return groupsRepository.getGroupById(API_VERSION, id, fields)
    }

    suspend fun getGroupWall(id: Int, offset: Int): List<WallItem> {
        val count = 10
        val fields = listOf( "photo_50", "name")
        val extended = 1
        return groupsRepository.getWallById(API_VERSION, -id, offset, count, extended, fields)
    }

    suspend fun likeAnItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsRepository.likeAnItem(API_VERSION, ownerId, itemId, type)
    }


    suspend fun removeLikeFromItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsRepository.removeLikeFromItem(API_VERSION, ownerId, itemId, type)
    }
}