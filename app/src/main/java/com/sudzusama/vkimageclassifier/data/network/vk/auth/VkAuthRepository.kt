package com.sudzusama.vkimageclassifier.data.network.vk.auth

import android.app.Activity
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class VkAuthRepository @Inject constructor(private val VKTokenStorage: VKTokenStorage) :
    AuthRepository {

    override fun getToken(): String? {
        return VKTokenStorage.getToken()
    }

    override fun saveToken(token: String) {
        VKTokenStorage.saveToken(token)
    }

    override fun isLoggedIn(): Boolean {
        return VK.isLoggedIn()
    }

    override fun login(activityContext: Activity, scopes: Collection<VKScope>) {
        VK.login(activityContext, scopes)
    }

    override fun logout() {
        VK.logout()
    }
}