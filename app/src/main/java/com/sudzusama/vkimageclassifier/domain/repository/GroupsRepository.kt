package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.Group

interface GroupsRepository {
    suspend fun getGroups(version: String, userId: String, extended: Int): List<Group>
}