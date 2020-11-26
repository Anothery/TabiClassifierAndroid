package com.sudzusama.vkimageclassifier.ui.groups

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.Group
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class GroupsViewModel @ViewModelInject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : BaseViewModel(authInteractor) {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> get() = _groups

    fun onGroupClicked(id: Int) {
        // TODO onGroupClicked
    }

    fun onSignOutItemClicked() {
        onUserLogout()
    }

    override fun onCreate() {
        viewModelScope.launch {
            val groupsResult = groupsInteractor.getGroups()
            _groups.value = groupsResult
        }
    }

}