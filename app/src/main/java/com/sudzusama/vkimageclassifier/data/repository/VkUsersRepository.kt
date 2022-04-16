package com.sudzusama.vkimageclassifier.data.repository

import com.sudzusama.vkimageclassifier.BuildConfig
import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.vk.UsersApi
import com.sudzusama.vkimageclassifier.domain.model.UserShort
import com.sudzusama.vkimageclassifier.domain.repository.UsersRepository
import javax.inject.Inject

class VkUsersRepository @Inject constructor(private val usersApi: UsersApi) : UsersRepository {
    override suspend fun getUserInfo(userId: Long, fields: List<String>): UserShort? {
        val user = usersApi.getUsers(
            BuildConfig.VK_API_VERSION,
            userId,
            fields.joinToString(",")
        ).response?.firstOrNull()?.mapToDomain()
        return user
    }
}