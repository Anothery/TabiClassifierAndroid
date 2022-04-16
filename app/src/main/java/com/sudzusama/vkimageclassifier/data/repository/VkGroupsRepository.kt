package com.sudzusama.vkimageclassifier.data.repository

import android.net.Uri
import com.sudzusama.vkimageclassifier.BuildConfig
import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.data.network.vk.VkException
import com.sudzusama.vkimageclassifier.data.response.vk.VkResponse
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import com.sudzusama.vkimageclassifier.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import javax.inject.Inject
import kotlin.math.abs


class VkGroupsRepository @Inject constructor(
    private val fileUtils: FileUtils,
    private val groupsApi: GroupsApi
) : GroupsRepository {
    override suspend fun getGroups(
        userId: Long,
        extended: Int,
        filter: String?,
        fields: List<String>
    ): List<GroupShort> {
        val groups =
            groupsApi.getGroups(
                BuildConfig.VK_API_VERSION,
                userId,
                extended,
                filter,
                fields.joinToString(",")
            )
        return getResponseOrThrow(groups).mapToDomain()
    }

    override suspend fun getGroupById(
        groupId: Int,
        fields: List<String>
    ): GroupDetail {
        val group =
            groupsApi.getGroupById(BuildConfig.VK_API_VERSION, groupId, fields.joinToString(","))
        return getResponseOrThrow(group).mapToDomain()
    }

    override suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem> {
        val wall = groupsApi.getWallById(
            BuildConfig.VK_API_VERSION,
            groupId,
            offset,
            count,
            extended,
            fields?.joinToString(",")
        )
        return getResponseOrThrow(wall).mapToDomain()
    }


    override suspend fun uploadPhotos(
        groupId: Int,
        photos: List<Picture>
    ): List<String> {
        val uploadServerUrl =
            getResponseOrThrow(
                groupsApi.getUploadServer(
                    BuildConfig.VK_API_VERSION,
                    groupId
                )
            ).uploadUrl
        val uploadedPhotos = mutableListOf<String>()
        photos.forEach { photo ->

            val content = fileUtils.contentFileToByteUtils(Uri.parse(photo.uri))
            val name = fileUtils.getFileName(Uri.parse(photo.uri)) ?: ""
            val body = MultipartBody.Part.createFormData(
                "photo",
                name,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), content)
            )


            val uploadedResult =
                groupsApi.uploadFileToServer(uploadServerUrl, BuildConfig.VK_API_VERSION, body)
            val wallResponse = groupsApi.saveWallPhoto(
                BuildConfig.VK_API_VERSION,
                groupId,
                uploadedResult.photo,
                uploadedResult.server,
                uploadedResult.hash
            )

            getResponseOrThrow(wallResponse).firstOrNull()
                ?.let { uploadedPhotos.add("photo${it.ownerId}_${it.id}") }
        }
        return uploadedPhotos
    }


    override suspend fun postToWall(
        ownerId: Int,
        fromGroup: Int,
        message: String?,
        attachements: List<String>?,
        publishDate: Long?
    ): Int {
        return getResponseOrThrow(
            groupsApi.postToWall(
                BuildConfig.VK_API_VERSION,
                -abs(ownerId),
                fromGroup,
                message,
                attachements?.joinToString(","),
                publishDate
            )
        ).postId
    }

    override suspend fun likeAnItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsApi.likeAnItem(BuildConfig.VK_API_VERSION, ownerId, itemId, type)
    }


    override suspend fun removeLikeFromItem(
        ownerId: Int,
        itemId: Int,
        type: String
    ) {
        groupsApi.removeLikeFromItem(BuildConfig.VK_API_VERSION, ownerId, itemId, type)
    }

    override suspend fun deletePost(groupId: Int, postId: Int): Boolean {
        val deleteResult = groupsApi.deletePost(BuildConfig.VK_API_VERSION, -abs(groupId), postId)
        return getResponseOrThrow(deleteResult) == 1
    }

    private fun <R : Any> getResponseOrThrow(response: VkResponse<R>): R {
        when {
            response.error != null -> throw VkException(
                response.error.errorCode,
                response.error.errorMsg
            )
            response.response != null -> return response.response
            else -> throw JSONException("Unknown JSON")
        }
    }
}
