package com.sudzusama.vkimageclassifier.data.repository

import android.net.Uri
import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.tabi.TabiApi
import com.sudzusama.vkimageclassifier.domain.model.ClassifyResponse
import com.sudzusama.vkimageclassifier.domain.repository.ClassifyRepository
import com.sudzusama.vkimageclassifier.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class TabiRepository @Inject constructor(
    private val fileUtils: FileUtils,
    private val tabiApi: TabiApi
) : ClassifyRepository {
    override suspend fun classifyImage(uri: String, isInternal: Boolean): ClassifyResponse {
        val fileType = "file"
        val content = fileUtils.contentFileToByteUtils(Uri.parse(uri))
        val name = fileUtils.getFileName(Uri.parse(uri)) ?: ""
        val body = MultipartBody.Part.createFormData(
            fileType,
            name,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), content))
        return tabiApi.classifyImage(body).mapToDomain()
    }
}