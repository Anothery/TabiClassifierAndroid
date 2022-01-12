package com.sudzusama.vkimageclassifier.ui.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.utils.FileUtils
import com.sudzusama.vkimageclassifier.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    authInteractor: AuthInteractor,
    fileUtils: FileUtils
) : BaseViewModel(authInteractor) {

    private val _galleryItems = MutableLiveData<List<GalleryItem>>()
    val galleryItems: LiveData<List<GalleryItem>> get() = _galleryItems

    private val _selectedItem = SingleLiveEvent<Int>()
    val selectedItem: LiveData<Int> get() = _selectedItem

    private val _deselectedItem = SingleLiveEvent<Int>()
    val deselectedItem: LiveData<Int> get() = _deselectedItem

    init {
        viewModelScope.launch {
            _galleryItems.value = fileUtils.findMediaFiles().map { GalleryItem("$it", false) }.reversed()
        }
    }

    fun onGalleryItemClicked(item: GalleryItem, position: Int) {
        if (item.selected) _deselectedItem.value = position else _selectedItem.value = position
    }
}