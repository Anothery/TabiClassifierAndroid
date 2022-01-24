package com.sudzusama.vkimageclassifier.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor

abstract class BaseViewModel constructor(private val authInteractor: AuthInteractor) : ViewModel() {
    protected val _showMessage = MutableLiveData<String>()
    val showMessage: LiveData<String> = _showMessage

    protected fun onUserLogout() {
        authInteractor.logout()
    }
}