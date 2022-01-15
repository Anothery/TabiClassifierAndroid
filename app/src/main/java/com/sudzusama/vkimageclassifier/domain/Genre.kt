package com.sudzusama.vkimageclassifier.domain

sealed class Genre {
    object Art : Genre()
    object Manga : Genre()
    object Frame : Genre()
    object Unknown : Genre()
}