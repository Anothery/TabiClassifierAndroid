package com.sudzusama.vkimageclassifier.data.network.vk

import com.sudzusama.vkimageclassifier.data.response.vk.GetUserResponse
import com.sudzusama.vkimageclassifier.data.response.vk.VkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("users.get")
    suspend fun getUsers(
        @Query("v") version: String,
        @Query("user_id") userId: Long,
        @Query("fields") fields: String
    ): VkResponse<List<GetUserResponse>>
}