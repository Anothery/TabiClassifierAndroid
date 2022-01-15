package com.sudzusama.vkimageclassifier.domain.repository

import com.sudzusama.vkimageclassifier.domain.model.ClassifyResponse

interface ClassifyRepository {
    suspend fun classifyImage(uri: String): ClassifyResponse
}