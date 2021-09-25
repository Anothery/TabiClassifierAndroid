package com.sudzusama.vkimageclassifier.ui.auth

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.vk.api.sdk.auth.VKAccessToken

class AuthViewModel @ViewModelInject constructor(private val authInteractor: AuthInteractor) :
    BaseViewModel(authInteractor) {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun onLoginSuccess(token: VKAccessToken) {
        authInteractor.saveSession(token.accessToken, token.userId)
    }

    fun onLogin(activityContext: Activity) {
        authInteractor.login(activityContext)
    }

    fun onError(errorCode: Int) {
        val message = "Login error. Error code: $errorCode"
        _errorMessage.value = message
    }


}