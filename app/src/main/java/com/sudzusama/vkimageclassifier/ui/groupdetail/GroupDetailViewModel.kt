package com.sudzusama.vkimageclassifier.ui.groupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
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

    private var wallId: Int = -1

    fun initWallItem(id: Int) {
        wallId = id
    }

    fun getGroupById() {
        viewModelScope.launch {
            _details.value = groupsInteractor.getGroupById(wallId)
        }
    }

    fun getFirstWallItems() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val loadedData = groupsInteractor.getGroupWall(wallId, wallItems.value?.size ?: 0)
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
            else _wallItems.value = list.apply { addAll(loadedData) }
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
}