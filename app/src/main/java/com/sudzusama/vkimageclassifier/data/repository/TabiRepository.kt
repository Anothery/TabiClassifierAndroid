package com.sudzusama.vkimageclassifier.data.repository

import com.sudzusama.vkimageclassifier.data.mapper.mapToDomain
import com.sudzusama.vkimageclassifier.data.network.tabi.TabiApi
import com.sudzusama.vkimageclassifier.domain.model.ClassifyResponse
import com.sudzusama.vkimageclassifier.domain.repository.ClassifyRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class TabiRepository @Inject constructor(private val tabiApi: TabiApi) : ClassifyRepository {
    override suspend fun classifyImage(uri: String): ClassifyResponse {
        val file = File(uri)
        val body = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        )
        return tabiApi.classifyImage(body).mapToDomain()
    }
}