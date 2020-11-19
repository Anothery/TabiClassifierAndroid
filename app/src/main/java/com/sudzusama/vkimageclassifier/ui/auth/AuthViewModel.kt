package com.sudzusama.vkimageclassifier.ui.auth

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel

class AuthViewModel @ViewModelInject constructor(private val authInteractor: AuthInteractor) :
    BaseViewModel(authInteractor) {

    override fun onCreate() {}

    fun onLoginSuccess(token: String) {
        authInteractor.saveToken(token)
    }

    fun onLogin(activityContext: Activity) {
        authInteractor.login(activityContext)
    }

    fun onError(errorCode: Int) {
        TODO("Not Implemented")
    }



}