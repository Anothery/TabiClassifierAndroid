package com.sudzusama.vkimageclassifier.domain.repository

import android.app.Activity
import com.vk.api.sdk.auth.VKScope

interface AuthRepository {
    fun getToken() : String?
    fun saveToken(token: String)
    fun isLoggedIn(): Boolean
    fun login(activityContext: Activity, scopes: Collection<VKScope>)
    fun logout()
}