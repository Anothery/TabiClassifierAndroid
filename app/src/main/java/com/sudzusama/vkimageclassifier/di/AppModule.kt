package com.sudzusama.vkimageclassifier.di

import android.content.Context
import android.content.SharedPreferences
import com.sudzusama.vkimageclassifier.utils.FileUtils
import com.sudzusama.vkimageclassifier.utils.view.VkSpannableHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("tabi-classifier-preferences", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFileUtils(@ApplicationContext context: Context): FileUtils = FileUtils(context)

    @Provides

    @Singleton
    fun provideVkSpanHelper(@ApplicationContext context: Context) = VkSpannableHelper(context)
}