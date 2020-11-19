package com.sudzusama.vkimageclassifier.ui.groups

import androidx.hilt.lifecycle.ViewModelInject
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel

class GroupsViewModel @ViewModelInject constructor(authInteractor: AuthInteractor) :
    BaseViewModel(authInteractor) {

    fun onSignOutButtonClicked() {
        onUserLogout()
    }

    override fun onCreate() {}

}