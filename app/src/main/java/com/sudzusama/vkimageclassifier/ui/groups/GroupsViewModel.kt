package com.sudzusama.vkimageclassifier.ui.groups

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.GroupShort
import com.sudzusama.vkimageclassifier.domain.model.GroupTypes
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.utils.view.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : BaseViewModel(authInteractor) {

    private val _groups = MutableLiveData<List<GroupShort>>()
    val groups: LiveData<List<GroupShort>> get() = _groups

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _showGroupDetail = SingleLiveEvent<GroupShort>()
    val showGroupDetail: LiveData<GroupShort> get() = _showGroupDetail

    private val _showSendPost = SingleLiveEvent<Pair<GroupDetail, Uri?>>()
    val showSendPost: LiveData<Pair<GroupDetail, Uri?>> get() = _showSendPost

    private val _exit = SingleLiveEvent<Boolean>()
    val exit: LiveData<Boolean> get() = _exit

    fun onSignOutItemClicked() {
        onUserLogout()
    }

    fun onGroupClicked(group: GroupShort, intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                Intent.ACTION_MAIN -> {
                    _showGroupDetail.value = group
                }
                Intent.ACTION_SEND -> {
                    if (intent.action == Intent.ACTION_SEND && intent.type != null) {
                        if (group.canPost || group.type == GroupTypes.PAGE && !group.canPost) {
                            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                            _showSendPost.value = Pair(
                                GroupDetail(
                                    group.id,
                                    group.name,
                                    group.photo50,
                                    group.photo100,
                                    group.photo200,
                                    group.isAdmin,
                                    group.canPost,
                                    group.type
                                ), uri
                            )
                        } else {
                            _showMessage.value =
                                "В данную группу нельзя запостить или предложить пост"
                        }
                    } else {
                        _showMessage.value = "Передан некорректный файл"
                    }
                }
                else -> _showGroupDetail.value = group
            }
        } else {
            _showGroupDetail.value = group
        }

    }


    init {
        viewModelScope.launch {
            try {
                _loading.value = true
                val groupsResult = groupsInteractor.getGroups()
                _groups.value = groupsResult
            } catch (ex: Exception) {
                ex.printStackTrace()
                if (ex is UnknownHostException) _showMessage.value = "Нет соединения с сервером VK"
                else _showMessage.value = ex.message
            } finally {
                _loading.value = false
            }
        }
    }
}