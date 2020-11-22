package com.sudzusama.vkimageclassifier.utils.network

import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val repository: AuthRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()

        repository.getToken()?.let {
            url.addQueryParameter("access_token", repository.getToken())
        }
        
        val request = original.newBuilder().url(url.build()).build()
        return chain.proceed(request)
    }
}