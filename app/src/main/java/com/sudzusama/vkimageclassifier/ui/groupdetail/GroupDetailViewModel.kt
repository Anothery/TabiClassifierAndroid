package com.sudzusama.vkimageclassifier.ui.groupdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import kotlinx.coroutines.launch

class GroupDetailViewModel @ViewModelInject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : ViewModel() {
    private val _details = MutableLiveData<GroupDetail>()
    val details: LiveData<GroupDetail> get() = _details

    fun getGroupById(id: Long) {
        viewModelScope.launch {
            _details.value = groupsInteractor.getGroupById(id)
        }
    }
}