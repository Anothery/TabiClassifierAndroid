package com.sudzusama.vkimageclassifier.di

import android.content.SharedPreferences
import com.sudzusama.vkimageclassifier.data.local.preferences.VKSessionPreferences
import com.sudzusama.vkimageclassifier.data.network.tabi.TabiApi
import com.sudzusama.vkimageclassifier.data.network.vk.GroupsApi
import com.sudzusama.vkimageclassifier.data.network.vk.UsersApi
import com.sudzusama.vkimageclassifier.data.repository.TabiRepository
import com.sudzusama.vkimageclassifier.data.repository.VkAuthRepository
import com.sudzusama.vkimageclassifier.data.repository.VkGroupsRepository
import com.sudzusama.vkimageclassifier.data.repository.VkUsersRepository
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.ClassifyRepository
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import com.sudzusama.vkimageclassifier.domain.repository.UsersRepository
import com.sudzusama.vkimageclassifier.utils.FileUtils
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
    fun provideGroupsRepository(
        fileUtils: FileUtils,
        groupsApi: GroupsApi
    ): GroupsRepository = VkGroupsRepository(fileUtils, groupsApi)

    @Provides
    @Singleton
    fun provideAuthRepository(vkSessionPreferences: VKSessionPreferences): AuthRepository =
        VkAuthRepository(vkSessionPreferences)

    @Provides
    @Singleton
    fun provideTabiRepository(fileUtils: FileUtils, tabiApi: TabiApi): ClassifyRepository =
        TabiRepository(fileUtils, tabiApi)


    @Provides
    @Singleton
    fun provideUsersRepository(usersApi: UsersApi): UsersRepository = VkUsersRepository(usersApi)

    @Provides
    @Singleton
    fun provideAuthStorage(preferences: SharedPreferences) = VKSessionPreferences(preferences)


}