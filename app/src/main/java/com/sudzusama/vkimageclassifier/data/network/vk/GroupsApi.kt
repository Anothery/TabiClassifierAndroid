package com.sudzusama.vkimageclassifier.data.network.vk

import com.sudzusama.vkimageclassifier.data.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @GET("photos.getWallUploadServer")
    suspend fun getUploadServer(
        @Query("v") version: String,
        @Query("group_id") groupId: Int
    ): GetUploadServerResponse

    @Multipart
    @POST
    suspend fun uploadFileToServer(
        @Url url: String,
        @Query("v") version: String,
        @Part photo: MultipartBody.Part,
    ): UploadFileResponse


    @GET("photos.saveWallPhoto")
    suspend fun saveWallPhoto(
        @Query("v") version: String,
        @Query("group_id") groupId: Int,
        @Query("photo") photo: String,
        @Query("server") server: Int,
        @Query("hash") hash: String,
    ): SaveWallResponse

    @GET("wall.post")
    suspend fun postToWall(
        @Query("v") version: String,
        @Query("owner_id") ownerId: Int,
        @Query("from_group") fromGroup: Int,
        @Query("message") message: String?,
        @Query("attachments") attachments: String?,
        @Query("publish_date") publishDate: Long?,
    ): WallPostResponse


}