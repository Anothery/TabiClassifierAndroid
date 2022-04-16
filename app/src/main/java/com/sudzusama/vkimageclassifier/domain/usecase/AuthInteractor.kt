package com.sudzusama.vkimageclassifier.domain.usecase

import android.app.Activity
import com.sudzusama.vkimageclassifier.domain.model.UserShort
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.UsersRepository
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {

    fun getLoginStateFlow() = authRepository.getLoginStateFlow()

    fun saveSession(token: String, userId: Long) {
        authRepository.saveSession(token, userId)
    }

    fun getUserId(): Long = authRepository.getUserId()

    suspend fun getCurrentUser(): UserShort? =
        usersRepository.getUserInfo(getUserId(), listOf("photo_200"))

    fun login(activityContext: Activity) {
        val scopes = listOf(VKScope.GROUPS, VKScope.WALL, VKScope.PHOTOS)
        authRepository.login(activityContext, scopes)
    }

    fun logout() {
        authRepository.logout()
    }
}