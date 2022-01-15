package com.sudzusama.vkimageclassifier.ui.createpost.pictures

import com.sudzusama.vkimageclassifier.domain.Genre
import com.sudzusama.vkimageclassifier.utils.view.dominantcolor.DominantColor

data class Picture(
    val uri: String,
    val galleryPosition: Int,
    val isLoading: Boolean,
    val detail: PictureDetail?
)

data class PictureDetail(val genre: Genre, val colors: List<DominantColor>)
