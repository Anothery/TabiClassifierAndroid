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
        @Query("user_id") userId: Int,
        @Query("extended") extended: Int,
        @Query("filter") filter: String?,
        @Query("fields") fields: String
    ): GroupsListResponse


    @GET("groups.getById")
    suspend fun getGroupById(
        @Query("v") version: String,
        @Query("group_id") userId: Int,
        @Query("fields") fields: String?
    ): GroupDetailResponse

    @GET("wall.get")
    suspend fun getWallById(
        @Query("v") version: String,
        @Query("owner_id") groupId: Int,
        @Query("offset") offset: Int,
        @Query("count") count: Int,
        @Query("extended") extended: Int?,
        @Query("fields") fields: String?
    ): GroupWallResponse

    @GET("likes.add")
    suspend fun likeAnItem(
        @Query("v") version: String,
        @Query("owner_id") owner_id: Int,
        @Query("item_id") item_id: Int,
        @Query("type") type: String
    )

    @GET("likes.delete")
    suspend fun removeLikeFromItem(
        @Query("v") version: String,
        @Query("owner_id") owner_id: Int,
        @Query("item_id") item_id: Int,
        @Query("type") type: String
    )
}