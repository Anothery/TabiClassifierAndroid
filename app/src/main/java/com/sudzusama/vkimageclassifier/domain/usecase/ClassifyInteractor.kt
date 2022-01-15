package com.sudzusama.vkimageclassifier.domain.usecase

import com.sudzusama.vkimageclassifier.domain.model.ClassifyResponse
import com.sudzusama.vkimageclassifier.domain.repository.ClassifyRepository
import javax.inject.Inject

class ClassifyInteractor @Inject constructor(private val tabiRepository: ClassifyRepository) {
    suspend fun classifyImage(uri: String): ClassifyResponse {
        return tabiRepository.classifyImage(uri)
    }
}