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
    }

    suspend fun getGroups(): List<GroupShort> {
        val fields = listOf("activity")
        return groupsRepository.getGroups(API_VERSION, authInteractor.getUserId(), EXTENDED, fields)
    }

    suspend fun getGroupById(id: Long): GroupDetail {
        val fields = listOf("description", "can_post", "ban_info", "members_count")
        return groupsRepository.getGroupById(API_VERSION, id, fields)
    }

    suspend fun getGroupWall(id: Long, offset: Int, count: Int): List<WallItem> {
        return groupsRepository.getWallById(API_VERSION, -id , offset, count)
    }
}