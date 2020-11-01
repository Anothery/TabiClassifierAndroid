package com.sudzusama.vkimageclassifier.ui.auth

import android.app.Activity
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(private val authInteractor: AuthInteractor) :
    ViewModel() {

    val isAuthorized = MutableLiveData<Boolean>()

    init {
        isAuthorized.postValue(authInteractor.isLoggedIn())
    }

    fun onAuth(activityContext: Activity) {
        authInteractor.login(activityContext)
    }

    fun onLogin(token: String) {
        authInteractor.saveToken(token)
        isAuthorized.postValue(true)
    }

    fun onError(errorCode: Int) {
        TODO("Not Implemented")
    }
}