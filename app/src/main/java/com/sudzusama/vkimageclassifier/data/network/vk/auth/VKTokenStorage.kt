package com.sudzusama.vkimageclassifier.data.network.vk.auth

import android.content.Context
import javax.inject.Inject

class VKTokenStorage @Inject constructor(context: Context) {
    private val preferences =
        context.getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val AUTH_PREFS_NAME = "TabiClassifier:auth"
        private const val AUTH_PREFS_TOKEN = "TabiClassifier:auth:token"
    }

    fun getToken(): String? {
        return preferences.getString(AUTH_PREFS_TOKEN, null)
    }

    fun saveToken(token: String) {
        preferences.edit().putString(AUTH_PREFS_TOKEN, token).apply()
    }
}