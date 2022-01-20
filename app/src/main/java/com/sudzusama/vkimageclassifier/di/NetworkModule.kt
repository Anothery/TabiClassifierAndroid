package com.sudzusama.vkimageclassifier.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sudzusama.vkimageclassifier.BuildConfig
import com.sudzusama.vkimageclassifier.data.network.tabi.TabiApi
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.domain.NetworkConstants
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.utils.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideGroupsApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): GroupsApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .baseUrl(BuildConfig.VK_API_URL)
        .build().create(GroupsApi::class.java)

    @Provides
    @Singleton
    fun provideTabiApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): TabiApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .baseUrl(BuildConfig.TABI_API_URL)
            .build().create(TabiApi::class.java)


    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        Json { ignoreUnknownKeys = true; }.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(authInterceptor)
        //     .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .connectTimeout(NetworkConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(NetworkConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(repository: AuthRepository) = AuthInterceptor(repository)
}