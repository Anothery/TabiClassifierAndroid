package com.sudzusama.vkimageclassifier.ui.groupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.model.WallItem
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor,
    authInteractor: AuthInteractor
) : ViewModel() {
    private val _details = MutableLiveData<GroupDetail>()
    val details: LiveData<GroupDetail> get() = _details

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    fun getGroupById(id: Int) {
        viewModelScope.launch {
            _details.value = groupsInteractor.getGroupById(id)
        }
    }

    fun getWallItems(id: Int, offset: Int) {
        viewModelScope.launch {
            _wallItems.value = groupsInteractor.getGroupWall(id, offset)
        }
    }

    fun onPostLiked(itemId: Int, isLiked: Boolean) {
        viewModelScope.launch {
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
                    groupsInteractor.likeAnItem(-group.id, itemId, GroupsInteractor.LIKE_TYPE_POST)
                }
            }

        }
    }
}