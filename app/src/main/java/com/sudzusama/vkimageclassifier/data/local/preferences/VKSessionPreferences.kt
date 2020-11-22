package com.sudzusama.vkimageclassifier.data.local.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class VKSessionPreferences @Inject constructor(private val preferences: SharedPreferences) {
    companion object {
        const val AUTH_TOKEN = "PREF_AUTH_TOKEN"
        const val USER_ID = "PREF_USER_ID"
    }

    fun getToken(): String? = preferences.getString(AUTH_TOKEN, "")
    fun getUserId(): String? = preferences.getString(USER_ID, "")

    fun saveSession(token: String, userId: String) {
        preferences.edit { putString(AUTH_TOKEN, token) }
        preferences.edit { putString(USER_ID, userId) }
    }

    fun clearSession() {
        preferences.edit { remove(AUTH_TOKEN) }
        preferences.edit { remove(USER_ID) }
    }

}