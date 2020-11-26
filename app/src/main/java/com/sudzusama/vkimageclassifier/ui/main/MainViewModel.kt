package com.sudzusama.vkimageclassifier.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.vk.api.sdk.utils.VKUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(authInteractor: AuthInteractor) :
    BaseViewModel(authInteractor) {

    private val _toLoginFlow = MutableLiveData<Boolean>()
    val toLoginFlow: LiveData<Boolean> get() = _toLoginFlow

    private val _toMainFlow = MutableLiveData<Boolean>()
    val toMainFlow get() = _toMainFlow

    private val loginState = authInteractor.getLoginStateFlow()

    override fun onCreate() {
        viewModelScope.launch {
            loginState.collect {
                when (it) {
                    false -> _toLoginFlow.value = true
                    true -> _toMainFlow.value = true
                }
            }
        }
    }

}