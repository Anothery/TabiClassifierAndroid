package com.sudzusama.vkimageclassifier.domain.usecase

import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import javax.inject.Inject
import kotlin.math.abs

class GroupsInteractor @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val groupsRepository: GroupsRepository
) {
    companion object {
        private const val EXTENDED = 1
        const val LIKE_TYPE_POST = "post"
        const val FROM_GROUP = 1
        const val MAX_PICTURES_PER_POST = 10
    }

    suspend fun getGroups(): List<GroupShort> {
        val fields = listOf("activity", "can_post")
        val filter = "admin,moder,editor"
        val nonModGroups = groupsRepository.getGroups(
            authInteractor.getUserId(),
            EXTENDED,
            null,
            fields
        )
        val modGroups = groupsRepository.getGroups(
            authInteractor.getUserId(),
            EXTENDED,
            filter,
            fields
        )
        return arrayListOf<GroupShort>().apply { addAll(modGroups); addAll(nonModGroups) }
            .distinctBy { it.id }
    }

    suspend fun getGroupById(id: Int): GroupDetail {
        val fields = listOf("description", "can_post", "ban_info", "members_count")
        return groupsRepository.getGroupById(id, fields)
    }

    suspend fun getGroupWall(id: Int, offset: Int): List<WallItem> {
        val count = 10
        val fields = listOf("photo_50", "name")
        val extended = 1
        return groupsRepository.getWallById(-id, offset, count, extended, fields)
    }

    suspend fun likeAnItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsRepository.likeAnItem(ownerId, itemId, type)
    }

    suspend fun sendPost(
        groupId: Int,
        pictures: List<Picture>,
        message: String?,
        publishDate: Long?
    ): Int {
        val photos = groupsRepository.uploadPhotos(groupId, pictures)
        return groupsRepository.postToWall(groupId, FROM_GROUP, message, photos, publishDate)
    }

    suspend fun deletePost(groupId: Int, postId: Int): Boolean {
        return groupsRepository.deletePost(groupId, postId)
    }

    suspend fun removeLikeFromItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsRepository.removeLikeFromItem(ownerId, itemId, type)
    }

}