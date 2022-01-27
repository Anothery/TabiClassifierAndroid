package com.sudzusama.vkimageclassifier.ui.imagedetail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.utils.FileUtils
import com.sudzusama.vkimageclassifier.utils.view.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    authInteractor: AuthInteractor,
    private val fileUtils: FileUtils
) : BaseViewModel(authInteractor) {


    private val _shareImage = SingleLiveEvent<String>()
    val shareImage: LiveData<String> get() = _shareImage

    fun onSaveClicked(url: String, bitmap: Bitmap) = viewModelScope.launch {
        try {
            val extension = fileUtils.getImageExtensionFromUrl(url) ?: ""
            val storageDir = fileUtils.saveImageToExternalStorage(bitmap, extension)
            _showMessage.value = "Изображение сохранено в папку $storageDir"

        } catch (ex: Exception) {
            _showMessage.value = "Не удалось сохранить изображение"
        }
    }

    fun onShareClicked(url: String, bitmap: Bitmap) = viewModelScope.launch {
        try {
            val extension = fileUtils.getImageExtensionFromUrl(url) ?: ""
            val name = fileUtils.saveBitmapToCache(bitmap, extension)
            name?.let { _shareImage.value = it }
                ?: _showMessage.setValue("Не удалось отправить изображение")

        } catch (ex: Exception) {
            _showMessage.value = "Не удалось отправить изображение"
        }
    }
}