package com.sudzusama.vkimageclassifier.domain.usecase

import android.app.Activity
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) {
    fun getToken(): String? {
        return authRepository.getToken()
    }

    fun saveToken(token: String) {
        authRepository.saveToken(token)
    }

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun login(activityContext: Activity) {
        val scopes = listOf<VKScope>(VKScope.GROUPS)
        authRepository.login(activityContext, scopes)
    }

    fun logout() {
        authRepository.logout()
    }
}