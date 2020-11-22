package com.sudzusama.vkimageclassifier.data.network.vk

import com.sudzusama.vkimageclassifier.data.response.GroupsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupsApi {

    @GET("groups.get")
    suspend fun getGroups(
        @Query("v") version: String,
        @Query("user_id") userId: String,
        @Query("extended") extended: Int
    ): GroupsListResponse
}