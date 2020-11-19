package com.sudzusama.vkimageclassifier.domain.repository

import android.app.Activity
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun getToken() : String?
    fun saveToken(token: String)
    fun isLoggedIn(): Boolean
    fun login(activityContext: Activity, scopes: Collection<VKScope>)
    fun logout()
    fun getLoginStateFlow(): StateFlow<Boolean>
}