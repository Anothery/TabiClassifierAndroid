package com.sudzusama.vkimageclassifier.data.response.tabi


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TabiClassifyResponse(
    @SerialName("colors")
    val colors: List<Color> = listOf(),
    @SerialName("predictions")
    val predictions: Predictions
) {
    @Serializable
    data class Color(
        @SerialName("meanHexColor")
        val meanHexColor: String,
        @SerialName("name")
        val name: String,
        @SerialName("percentage")
        val percentage: Double
    )

    @Serializable
    data class Predictions(
        @SerialName("art")
        val art: Double,
        @SerialName("frame")
        val frame: Double,
        @SerialName("manga")
        val manga: Double
    )
}