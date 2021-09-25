package com.sudzusama.vkimageclassifier.data.repository

import android.app.Activity
import com.sudzusama.vkimageclassifier.data.local.preferences.VKSessionPreferences
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class VkAuthRepository @Inject constructor(private val vkSessionPreferences: VKSessionPreferences) :
    AuthRepository {

    private val loginState: MutableStateFlow<Boolean> = MutableStateFlow(isLoggedIn())

    override fun isLoggedIn(): Boolean = VK.isLoggedIn()

    override fun getLoginStateFlow(): StateFlow<Boolean> = loginState

    override fun login(activityContext: Activity, scopes: Collection<VKScope>) {
        VK.login(activityContext, scopes)
    }

    override fun logout() {
        VK.logout()
        vkSessionPreferences.clearSession()
        updateLoginState()
    }

    override fun getToken(): String? = vkSessionPreferences.getToken()
    override fun getUserId(): Int = vkSessionPreferences.getUserId()
    override fun saveSession(token: String, userId: Int) {
        vkSessionPreferences.saveSession(token, userId)
        updateLoginState()
    }

    private fun updateLoginState() {
        loginState.value = isLoggedIn()
    }
}