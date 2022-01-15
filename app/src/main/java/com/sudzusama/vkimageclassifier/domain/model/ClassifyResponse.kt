package com.sudzusama.vkimageclassifier.domain.model

data class ClassifyResponse(
    val colors: List<Color>,
    val predictions: Predictions
) {
    data class Color(
        val meanHexColor: String,
        val name: String,
        val percent: Double
    )

    data class Predictions(
        val art: Double,
        val frame: Double,
        val manga: Double
    )
}
