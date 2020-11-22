package com.sudzusama.vkimageclassifier.di

import com.sudzusama.vkimageclassifier.data.repository.VkAuthRepository
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideAuthInteractor(vkAuthRepository: VkAuthRepository) = AuthInteractor(vkAuthRepository)

    @Provides
    @Singleton
    fun provideGroupsInteractor(
        authInteractor: AuthInteractor,
        groupsRepository: GroupsRepository
    ) = GroupsInteractor(authInteractor, groupsRepository)
}