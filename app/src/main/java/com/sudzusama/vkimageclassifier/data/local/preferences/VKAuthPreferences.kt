package com.sudzusama.vkimageclassifier.data.local.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class VKAuthPreferences @Inject constructor(private val preferences: SharedPreferences) {
    companion object {
        const val AUTH_TOKEN = "PREF_AUTH_TOKEN"
    }

    fun getToken(): String? = preferences.getString(AUTH_TOKEN, null)


    fun saveToken(token: String) = preferences.edit { putString(AUTH_TOKEN, token) }

    fun removeToken() = preferences.edit { remove(AUTH_TOKEN) }

}