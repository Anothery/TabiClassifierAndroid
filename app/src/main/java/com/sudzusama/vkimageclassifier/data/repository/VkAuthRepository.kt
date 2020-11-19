package com.sudzusama.vkimageclassifier.data.repository

import android.app.Activity
import com.sudzusama.vkimageclassifier.data.local.preferences.VKAuthPreferences
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class VkAuthRepository @Inject constructor(private val vkAuthPreferences: VKAuthPreferences) :
    AuthRepository {

    private val loginState: MutableStateFlow<Boolean> = MutableStateFlow(isLoggedIn())

    override fun isLoggedIn(): Boolean = VK.isLoggedIn()

    override fun getLoginStateFlow(): StateFlow<Boolean> = loginState

    override fun login(activityContext: Activity, scopes: Collection<VKScope>) {
        VK.login(activityContext, scopes)
    }

    override fun logout() {
        VK.logout()
        vkAuthPreferences.removeToken()
        updateLoginState()
    }

    private fun updateLoginState() {
        loginState.value = isLoggedIn()
    }

    override fun getToken(): String? = vkAuthPreferences.getToken()
    override fun saveToken(token: String) {
        vkAuthPreferences.saveToken(token)
        updateLoginState()
    }
}