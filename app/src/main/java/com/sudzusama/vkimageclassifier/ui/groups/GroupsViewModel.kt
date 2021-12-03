package com.sudzusama.vkimageclassifier.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : BaseViewModel(authInteractor) {

    private val _groups = MutableLiveData<List<GroupShort>>()
    val groups: LiveData<List<GroupShort>> get() = _groups

    private val _showGroupDetail = SingleLiveEvent<Int>()
    val showGroupDetail: LiveData<Int> get() = _showGroupDetail


    fun onGroupClicked(id: Int) {
        _showGroupDetail.value = id
    }

    fun onSignOutItemClicked() {
        onUserLogout()
    }

    init {
        viewModelScope.launch {
            val groupsResult = groupsInteractor.getGroups()
            _groups.value = groupsResult
        }
    }
}