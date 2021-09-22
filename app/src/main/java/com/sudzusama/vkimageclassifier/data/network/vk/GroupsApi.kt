package com.sudzusama.vkimageclassifier.data.network.vk

import com.sudzusama.vkimageclassifier.data.response.GroupDetailResponse
import com.sudzusama.vkimageclassifier.data.response.GroupWallResponse
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

    @GET("wall.get")
    suspend fun getWallById(
        @Query("v") version: String,
        @Query("owner_id") groupId: Long,
        @Query("offset") offset: Int,
        @Query("count") count: Int
    ): GroupWallResponse
}