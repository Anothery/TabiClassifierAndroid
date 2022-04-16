package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.UserShort

interface UsersRepository {
    suspend fun getUserInfo(userId: Long, fields: List<String>): UserShort?
}