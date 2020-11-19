package com.sudzusama.vkimageclassifier.ui.base

import androidx.lifecycle.ViewModel
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor

abstract class BaseViewModel constructor(private val authInteractor: AuthInteractor) : ViewModel() {

    abstract fun onCreate()

    protected fun onUserLogout() {
        authInteractor.logout()
    }
}