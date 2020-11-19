package com.sudzusama.vkimageclassifier.di

import android.content.Context
import android.content.SharedPreferences
import com.sudzusama.vkimageclassifier.data.local.preferences.VKAuthPreferences
import com.sudzusama.vkimageclassifier.data.repository.VkAuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.AuthRepository
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
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
    fun provideAuthStorage(preferences: SharedPreferences) = VKAuthPreferences(preferences)

    @Provides
    @Singleton
    fun provideAuthInteractor(vkAuthRepository: VkAuthRepository) = AuthInteractor(vkAuthRepository)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("tabi-classifier-preferences", Context.MODE_PRIVATE)
}