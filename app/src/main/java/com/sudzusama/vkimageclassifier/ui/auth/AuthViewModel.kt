package com.sudzusama.vkimageclassifier.ui.auth

import android.app.Activity
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.exceptions.VKAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authInteractor: AuthInteractor) :
    BaseViewModel(authInteractor) {

    fun onLoginSuccess(token: VKAccessToken) {
        authInteractor.saveSession(token.accessToken, token.userId.value)
    }

    fun onLogin(activityContext: Activity) {
        authInteractor.login(activityContext)
    }

    fun onError(authException: VKAuthException) {
        val message = "Login error: ${authException.message}"
        _showMessage.value = message
    }
}