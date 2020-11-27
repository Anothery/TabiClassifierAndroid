package com.sudzusama.vkimageclassifier.domain.usecase

import com.sudzusama.vkimageclassifier.domain.model.Group
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import javax.inject.Inject

class GroupsInteractor @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val groupsRepository: GroupsRepository
) {
    companion object {
        private const val API_VERSION = "5.69"
        private const val EXTENDED = 1
    }

    suspend fun getGroups(): List<Group> {
        val fields = listOf("activity")
        return groupsRepository.getGroups(API_VERSION, authInteractor.getUserId() ?: "", EXTENDED, fields)
    }
}