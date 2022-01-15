package com.sudzusama.vkimageclassifier.data.network.tabi

import com.sudzusama.vkimageclassifier.data.response.TabiClassifyResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface TabiApi {
    @Multipart
    @POST("classify")
    suspend fun classifyImage(@Part file: MultipartBody.Part): TabiClassifyResponse
}