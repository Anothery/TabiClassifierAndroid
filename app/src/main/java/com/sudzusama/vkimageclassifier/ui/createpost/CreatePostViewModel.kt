package com.sudzusama.vkimageclassifier.ui.createpost

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sudzusama.vkimageclassifier.domain.Genre
import com.sudzusama.vkimageclassifier.domain.model.GroupDetail
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.ClassifyInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import com.sudzusama.vkimageclassifier.ui.base.BaseViewModel
import com.sudzusama.vkimageclassifier.ui.createpost.gallery.GalleryItem
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.Picture
import com.sudzusama.vkimageclassifier.ui.createpost.pictures.PictureDetail
import com.sudzusama.vkimageclassifier.ui.createpost.tags.Tag
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
    private val groupsInteractor: GroupsInteractor,
    fileUtils: FileUtils
) : BaseViewModel(authInteractor) {

    private val _galleryItems = MutableLiveData<List<GalleryItem>>()
    val galleryItems: LiveData<List<GalleryItem>> get() = _galleryItems

    private val _pictures = MutableLiveData<List<Picture>>(listOf())
    val pictures: LiveData<List<Picture>> get() = _pictures

    private val _genreTags = MutableLiveData<List<Tag>>(listOf())
    val genreTags: LiveData<List<Tag>> get() = _genreTags

    private val _colorTags = MutableLiveData<List<Tag>>(listOf())
    val colorTags: LiveData<List<Tag>> get() = _colorTags

    private val _selectedItem = SingleLiveEvent<Int>()
    val selectedItem: LiveData<Int> get() = _selectedItem

    private val _deselectedItem = SingleLiveEvent<Int>()
    val deselectedItem: LiveData<Int> get() = _deselectedItem

    private val _onPostSent = SingleLiveEvent<Boolean>()
    val onPostSent: LiveData<Boolean> get() = _onPostSent

    private val _tagsRecognition = MutableLiveData(true)
    val tagsRecognition: LiveData<Boolean> get() = _tagsRecognition

    private val _postingState = SingleLiveEvent<Boolean>()
    val postingState: LiveData<Boolean> get() = _postingState

    private var groupId: Int? = null

    init {
        viewModelScope.launch {
            _galleryItems.value =
                fileUtils.findMediaFiles().map { GalleryItem("$it", false) }.reversed()
        }
    }

    fun setGroupDetails(detail: GroupDetail?) {
        groupId = detail?.id ?: -1
    }

    fun onGalleryItemClicked(item: GalleryItem, position: Int) {
        if (item.selected) {
            pictures.value?.let { pictures ->
                val updatedList = pictures.toMutableList().apply {
                    pictures.firstOrNull { it.uri == item.uri }?.let {
                        remove(it)
                        _deselectedItem.value = position
                    }
                }
                _pictures.value = updatedList
                if (updatedList.isEmpty()) {
                    _colorTags.value = listOf()
                    _genreTags.value = listOf()
                }
            }

        } else {
            pictures.value?.let { pictures ->
                if (pictures.size >= GroupsInteractor.MAX_PICTURES_PER_POST) {
                    _errorMessage.value = "Нельзя добавить более 10 изображений"
                    return
                }
                viewModelScope.launch {
                    _selectedItem.value = position

                    if (_tagsRecognition.value != false) {
                        val picture = Picture(item.uri, position, true, null)
                        val updatedPictures = pictures.toMutableList().apply { add(picture) }
                        _pictures.value = updatedPictures
                        try {
                            val info = classifyInteractor.classifyImage(item.uri)
                            val genre = with(info.predictions) {
                                if (art >= manga && art >= frame) return@with Genre.Art(art)
                                else if (manga >= art && manga >= frame) return@with Genre.Manga(
                                    manga
                                )
                                else if (frame >= art && frame >= manga) return@with Genre.Frame(
                                    frame
                                )
                                else return@with Genre.Unknown(0.0)
                            }

                            val dominantColors = info.colors.map {
                                DominantColor(
                                    Color.parseColor(it.meanHexColor),
                                    it.percent,
                                    it.name
                                )
                            }

                            _pictures.value?.let { recent ->
                                recent.find { it.uri == picture.uri }?.let { picture ->
                                    updateGenreTags(
                                        info.predictions.art,
                                        info.predictions.manga,
                                        info.predictions.frame
                                    )
                                    updateColorTags(dominantColors, pictures)
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

                    } else {
                        val picture = Picture(item.uri, position, false, null)
                        val updatedPictures = pictures.toMutableList().apply { add(picture) }
                        _pictures.value = updatedPictures
                    }
                }
            }
        }
    }

    private fun updateColorTags(
        currentPictureColors: List<DominantColor>,
        pictures: List<Picture>
    ) {
        // TODO refactor & mb optimize
        _colorTags.value?.let { prevColors ->
            if (prevColors.isNotEmpty()) {
                val allDominantColors =
                    pictures.flatMap { it.detail?.colors ?: return }
                        .toMutableList().apply { addAll(currentPictureColors) }

                _colorTags.value =
                    allDominantColors.distinctBy { it.name }.map { dc ->
                        dc.copy(percent = allDominantColors.filter { adc -> adc.name == dc.name }
                            .map { it.percent }.sum())
                    }
                        .apply { sortedByDescending { it.percent } }
                        .mapIndexed { i, it -> Tag(it.name, it.color, i < 3) }
                        .sortedByDescending { it.selected }
            } else {
                _colorTags.value =
                    currentPictureColors.toMutableList().apply { sortByDescending { it.percent } }
                        .mapIndexed { i, it -> Tag(it.name, it.color, i < 3) }

            }
        }
    }

    private fun updateGenreTags(art: Double, frame: Double, manga: Double) {
        val checkedArt = art >= manga && art >= frame
        val checkedManga = manga >= art && manga >= frame
        val checkedFrame = frame >= manga && frame >= art
        val artTag = Tag("art", Color.GRAY, checkedArt && !checkedManga && !checkedFrame)
        val mangaTag = Tag("manga", Color.GRAY, checkedManga && !checkedArt && !checkedFrame)
        val frameTag = Tag("frame", Color.GRAY, checkedFrame && !checkedArt && !checkedManga)
        _genreTags.value =
            mutableListOf(artTag, mangaTag, frameTag).sortedByDescending { it.selected }
    }

    fun onRemovePictureClicked(picture: Picture) {
        pictures.value?.let { pictures ->
            val updatedList = pictures.toMutableList().apply {
                pictures.firstOrNull { it.uri == picture.uri }?.let {
                    remove(it)
                    _deselectedItem.value = picture.galleryPosition
                }
            }
            _pictures.value = updatedList
            if (updatedList.isEmpty()) {
                _colorTags.value = listOf()
                _genreTags.value = listOf()
            }
        }
    }

    fun onSaveButtonClicked() =
        viewModelScope.launch {
            pictures.value?.let { pictures ->
                try {
                    groupId?.let { groupId ->
                        if(pictures.firstOrNull{ it.isLoading } != null) {
                            _errorMessage.value = "Дождитесь окончания распознавания изображений"
                            return@launch
                        }

                        val selectedGenre = _genreTags.value?.firstOrNull { it.selected }
                        val selectedColors = _colorTags.value?.filter { it.selected }
                        val hasTags = !selectedColors.isNullOrEmpty() && selectedGenre != null
                        val hasPictures = pictures.isNotEmpty()
                        if(!hasTags && !hasPictures) {
                            _errorMessage.value = "Добавьте хотя бы одно изображение или тег"
                            return@launch
                        }
                        _postingState.value = true
                        var message = ""
                        if (selectedGenre != null) message = "#" + selectedGenre.name + "\n"
                        if (selectedColors != null && selectedColors.isNotEmpty()) {
                            selectedColors.forEachIndexed { i, it ->
                                message += "#" + it.name
                                if (i != selectedColors.lastIndex) message += " "
                            }
                        }
                        groupsInteractor.sendPost(
                            groupId,
                            pictures,
                            if (message.isNotEmpty()) message else null
                        )
                        _postingState.value = false
                        _onPostSent.value = true
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _errorMessage.value = ex.message
                    _postingState.value = false
                }
            }
        }


    fun onChangeTagsRecognition() {
        val enabled = tagsRecognition.value ?: true
        _tagsRecognition.value = !enabled
    }

    fun onSetDefaultTagsClicked() = viewModelScope.launch {
        _genreTags.value?.let { genres ->
            val newItems = listOf(
                Tag("art", Color.GRAY, false),
                Tag("manga", Color.GRAY, false),
                Tag("frame", Color.GRAY, false),
                Tag("gif", Color.GRAY, false),
                Tag("other", Color.GRAY, false),
            ).filter { !genres.any { g -> g.name == it.name } }

            _genreTags.value = genres.toMutableList().apply { addAll(newItems) }
        }
        _colorTags.value?.let { colors ->
            val newItems = listOf(
                Tag("bw", Color.BLACK, false),
                Tag("mixed", Color.GREEN, false),
                Tag("white", Color.WHITE, false),
                Tag("black", Color.BLACK, false),
                Tag("gray", Color.GRAY, false),
                Tag("red", Color.RED, false),
                Tag("orange", Color.parseColor("#FFA500"), false),
                Tag("pink", Color.parseColor("#FFC0CB"), false),
                Tag("violet", Color.parseColor("#EE82EE"), false),
                Tag("cyan", Color.CYAN, false),
                Tag("blue", Color.BLUE, false),
                Tag("yellow", Color.YELLOW, false),
                Tag("gold", Color.parseColor("#FFD700"), false),
                Tag("beige", Color.parseColor("#F5F5DC"), false),
                Tag("brown", Color.parseColor("#A52A2A"), false),
            ).filter { !colors.any { g -> g.name == it.name } }
            _colorTags.value = colors.toMutableList().apply { addAll(newItems) }
        }
    }

    fun onColorCheckUpdate(checked: Boolean, position: Int) = viewModelScope.launch {
        _colorTags.value?.let { colors ->
            _colorTags.value =
                colors.toMutableList()
                    .apply { set(position, get(position).copy(selected = checked)) }
                    .sortedByDescending { it.selected }
        }
    }

    fun onGenreCheckUpdate(checked: Boolean, position: Int) = viewModelScope.launch {
        _genreTags.value?.let { tags ->
            val test = tags.toMutableList().mapIndexed { i, it ->
                if (i == position) it.copy(selected = checked)
                else if (checked && it.selected) it.copy(selected = false)
                else it
            }.sortedByDescending { it.selected }
            _genreTags.value = test
        }
    }
}