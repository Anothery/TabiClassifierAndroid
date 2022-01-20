package com.sudzusama.vkimageclassifier.ui.groupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.utils.view.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : BaseViewModel(authInteractor) {
    private val _details = MutableLiveData<GroupDetail>()
    val details: LiveData<GroupDetail> get() = _details

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _downloadMore = MutableLiveData(true)
    val downloadMore: LiveData<Boolean> get() = _downloadMore

    private val _showStartProgress = MutableLiveData(true)
    val showStartProgress: LiveData<Boolean> get() = _showStartProgress

    private val _showCreateScreen = SingleLiveEvent<GroupDetail>()
    val showCreateScreen: LiveData<GroupDetail> get() = _showCreateScreen

    private var wallId: Int = -1


    fun initialize(id: Int) {
        wallId = id

        if (_wallItems.value == null) {
            getFirstWallItems()
            getGroupById()
        }
    }

    private fun getGroupById() {
        viewModelScope.launch {
            try {
                _details.value = groupsInteractor.getGroupById(wallId)
            } catch (ex: Exception) {
                _errorMessage.value = ex.message
            }
        }
    }

    fun getFirstWallItems() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val loadedData = groupsInteractor.getGroupWall(wallId, 0)
                if (loadedData.isEmpty()) _downloadMore.value = false
                _wallItems.value = loadedData
                _showStartProgress.value = false
            } catch (ex: Exception) {
                _errorMessage.value = ex.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onDownloadMore() = viewModelScope.launch {
        try {
            _isLoading.value = true
            val list = wallItems.value?.toMutableList() ?: mutableListOf()
            val loadedData = groupsInteractor.getGroupWall(wallId, wallItems.value?.size ?: 0)
            if (loadedData.isEmpty()) _downloadMore.value = false
            else _wallItems.value = list.apply { addAll(loadedData) }.distinctBy { it.id }
                .sortedByDescending { it.date }
        } catch (ex: Exception) {
            _errorMessage.value = ex.message
        } finally {
            _isLoading.value = false
        }
    }

    fun onPostLiked(itemId: Int, isLiked: Boolean) {
        viewModelScope.launch {
            try {
                if (isLiked) {
                    _details.value?.let { group ->
                        groupsInteractor.removeLikeFromItem(
                            -group.id,
                            itemId,
                            GroupsInteractor.LIKE_TYPE_POST
                        )
                    }
                } else {
                    _details.value?.let { group ->
                        groupsInteractor.likeAnItem(
                            -group.id,
                            itemId,
                            GroupsInteractor.LIKE_TYPE_POST
                        )
                    }
                }
            } catch (ex: Exception) {
                _errorMessage.value = ex.message
            }
        }
    }

    fun onUpdateWall() = viewModelScope.launch {
        try {
            val list = wallItems.value?.toMutableList() ?: mutableListOf()
            val loadedData = groupsInteractor.getGroupWall(wallId, 0)
            if (list.isEmpty()) _wallItems.value = loadedData.sortedByDescending { it.date }
            else _wallItems.value = list.apply { addAll(loadedData) }.distinctBy { it.id }
                .sortedByDescending { it.date }
        } catch (ex: Exception) {
            _errorMessage.value = ex.message
        }
    }

    fun onPostRemoved(id: Int) = viewModelScope.launch {
        _details.value?.let { details ->
            try {
                if (groupsInteractor.deletePost(details.id, id)) {
                    _wallItems.value =
                        _wallItems.value?.toMutableList()?.apply { removeAll { it.id == id } }
                } else {
                    _errorMessage.value = "Не удалось удалить пост"
                }
            } catch (ex: Exception) {
                _errorMessage.value = "Не удалось удалить пост"
            }
        }
    }

    fun onFabCreateClicked() {
        viewModelScope.launch { _showCreateScreen.value = _details.value }
    }
}