package com.sudzusama.vkimageclassifier.di

import android.content.SharedPreferences
import com.sudzusama.vkimageclassifier.data.local.preferences.VKSessionPreferences
import com.sudzusama.vkimageclassifier.data.repository.VkAuthRepository
import com.sudzusama.vkimageclassifier.data.repository.VkGroupsRepository
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideGroupsRepository(vkGroupsRepository: VkGroupsRepository): GroupsRepository =
        vkGroupsRepository

    @Provides
    @Singleton
    fun provideAuthRepository(vkAuthRepository: VkAuthRepository): AuthRepository = vkAuthRepository

    @Provides
    @Singleton
    fun provideAuthStorage(preferences: SharedPreferences) = VKSessionPreferences(preferences)
}