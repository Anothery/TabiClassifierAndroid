package com.sudzusama.vkimageclassifier.ui.createpost

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.Genre
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.ClassifyInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.PictureDetail
import com.sudzusama.vkimageclassifier.utils.FileUtils
import com.sudzusama.vkimageclassifier.utils.view.SingleLiveEvent
import com.sudzusama.vkimageclassifier.utils.view.dominantcolor.DominantColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    authInteractor: AuthInteractor,
    private val classifyInteractor: ClassifyInteractor,
    fileUtils: FileUtils
) : BaseViewModel(authInteractor) {

    private val _galleryItems = MutableLiveData<List<GalleryItem>>()
    val galleryItems: LiveData<List<GalleryItem>> get() = _galleryItems

    private val _pictures = MutableLiveData<List<Picture>>(listOf())
    val pictures: LiveData<List<Picture>> get() = _pictures

    private val _selectedItem = SingleLiveEvent<Int>()
    val selectedItem: LiveData<Int> get() = _selectedItem

    private val _deselectedItem = SingleLiveEvent<Int>()
    val deselectedItem: LiveData<Int> get() = _deselectedItem

    init {
        viewModelScope.launch {
            _galleryItems.value =
                fileUtils.findMediaFiles().map { GalleryItem("$it", false) }.reversed()
        }
    }

    fun onGalleryItemClicked(item: GalleryItem, position: Int) {
        if (item.selected) {
            pictures.value?.let { pictures ->
                _pictures.value = pictures.toMutableList().apply {
                    pictures.firstOrNull { it.uri == item.uri }?.let {
                        remove(it)
                        _deselectedItem.value = position
                    }
                }
            }

        } else {
            pictures.value?.let { pictures ->
                viewModelScope.launch {
                    _selectedItem.value = position
                    val picture = Picture(item.uri, position, true, null)
                    val updatedPictures = pictures.toMutableList().apply { add(picture) }
                    _pictures.value = updatedPictures
                    try {
                        val classifyInformation = classifyInteractor.classifyImage(item.uri)
                        val genre = with(classifyInformation.predictions) {
                            if (art >= manga && art >= frame) return@with Genre.Art
                            else if (manga >= art && manga >= frame) return@with Genre.Manga
                            else if (frame >= art && frame >= manga) return@with Genre.Frame
                            else return@with Genre.Unknown
                        }
                        val dominantColors = classifyInformation.colors.map {
                            DominantColor(Color.parseColor(it.meanHexColor), it.percent.toFloat())
                        }

                        _pictures.value?.let { recent ->
                            recent.find { it.uri == picture.uri }?.let { picture ->
                                _pictures.value = recent.toMutableList().apply {
                                    this[indexOf(picture)] = picture.copy(
                                        isLoading = false,
                                        detail = PictureDetail(genre, dominantColors)
                                    )
                                }
                            }
                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        _pictures.value?.let { recent ->
                            recent.find { it.uri == picture.uri }?.let { picture ->
                                _errorMessage.value = ex.message
                                _pictures.value = recent.toMutableList().apply {
                                    this[indexOf(picture)] = picture.copy(isLoading = false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onRemovePictureClicked(picture: Picture) {
        pictures.value?.let { pictures ->
            _pictures.value = pictures.toMutableList().apply {
                pictures.firstOrNull { it.uri == picture.uri }?.let {
                    remove(it)
                    _deselectedItem.value = picture.galleryPosition
                }
            }
        }
    }
}