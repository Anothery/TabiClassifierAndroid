package com.sudzusama.vkimageclassifier.di

import android.content.Context
import com.sudzusama.vkimageclassifier.data.network.vk.auth.VKTokenStorage
import com.sudzusama.vkimageclassifier.data.network.vk.auth.VkAuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(vkAuthRepository: VkAuthRepository): AuthRepository = vkAuthRepository

    @Provides
    @Singleton
    fun provideAuthStorage(@ApplicationContext context: Context) = VKTokenStorage(context)
}