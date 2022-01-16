package com.sudzusama.vkimageclassifier.domain

sealed class Genre {
    abstract val percentage: Double

    data class Art(override val percentage: Double) : Genre()
    data class Manga(override val percentage: Double) : Genre()
    data class Frame(override val percentage: Double) : Genre()
    data class Unknown(override val percentage: Double) : Genre()
}