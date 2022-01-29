package com.sudzusama.vkimageclassifier.domain.usecase

import android.graphics.Color
import com.sudzusama.vkimageclassifier.domain.model.ClassifyResponse
import com.sudzusama.vkimageclassifier.domain.repository.ClassifyRepository
import com.sudzusama.vkimageclassifier.ui.createpost.tags.Tag
import javax.inject.Inject

class ClassifyInteractor @Inject constructor(private val tabiRepository: ClassifyRepository) {
    suspend fun classifyImage(uri: String, isInternal: Boolean): ClassifyResponse {
        return tabiRepository.classifyImage(uri, isInternal)
    }

    val defaultGenreTags = listOf(
        Tag("art", Color.LTGRAY, false),
        Tag("manga", Color.LTGRAY, false),
        Tag("frame", Color.LTGRAY, false),
        Tag("gif", Color.LTGRAY, false),
        Tag("other", Color.LTGRAY, false),
    )

    val defaultColorTags = listOf(
        Tag("bw", Color.BLACK, false),
        Tag("mixed", Color.parseColor("#3c9970"), false),
        Tag("white", Color.WHITE, false),
        Tag("black", Color.BLACK, false),
        Tag("gray", Color.GRAY, false),
        Tag("red", Color.parseColor("#DC143C"), false),
        Tag("orange", Color.parseColor("#FFA500"), false),
        Tag("pink", Color.parseColor("#FFC0CB"), false),
        Tag("violet", Color.parseColor("#EE82EE"), false),
        Tag("cyan", Color.parseColor("#AFEEEE"), false),
        Tag("blue", Color.parseColor("#4169E1"), false),
        Tag("yellow", Color.parseColor("#FFFF99"), false),
        Tag("green", Color.parseColor("#228B22"), false),
        Tag("gold", Color.parseColor("#FFD700"), false),
        Tag("beige", Color.parseColor("#F5F5DC"), false),
        Tag("brown", Color.parseColor("#A52A2A"), false),
    )
}