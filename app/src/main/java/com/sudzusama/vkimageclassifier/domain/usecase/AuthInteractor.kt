package com.sudzusama.vkimageclassifier.domain.usecase

import android.app.Activity
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) {

    fun getLoginStateFlow() = authRepository.getLoginStateFlow()

    fun saveSession(token: String, userId: String) {
        authRepository.saveSession(token, userId)
    }

    fun getUserId() = authRepository.getUserId()

    fun login(activityContext: Activity) {
        val scopes = listOf(VKScope.GROUPS, VKScope.OFFLINE)
        authRepository.login(activityContext, scopes)
    }

    fun logout() {
        authRepository.logout()
    }

}