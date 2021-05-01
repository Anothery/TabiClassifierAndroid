package com.sudzusama.vkimageclassifier.data.network.vk

import com.sudzusama.vkimageclassifier.data.response.GroupDetailResponse
import com.sudzusama.vkimageclassifier.data.response.GroupsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupsApi {

    @GET("groups.get")
    suspend fun getGroups(
        @Query("v") version: String,
        @Query("user_id") userId: Long,
        @Query("extended") extended: Int,
        @Query("fields") fields: String
    ): GroupsListResponse


    @GET("groups.getById")
    suspend fun getGroupById(
        @Query("v") version: String,
        @Query("group_id") userId: Long,
        @Query("fields") fields: String
    ): GroupDetailResponse
}