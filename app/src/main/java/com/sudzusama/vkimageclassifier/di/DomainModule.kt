package com.sudzusama.vkimageclassifier.di

import com.sudzusama.vkimageclassifier.data.repository.VkAuthRepository
import com.sudzusama.vkimageclassifier.data.repository.VkUsersRepository
import com.sudzusama.vkimageclassifier.domain.repository.GroupsRepository
import com.sudzusama.vkimageclassifier.domain.usecase.AuthInteractor
import com.sudzusama.vkimageclassifier.domain.usecase.GroupsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideAuthInteractor(vkAuthRepository: VkAuthRepository, vkUsersRepository: VkUsersRepository) = AuthInteractor(vkAuthRepository,vkUsersRepository)

    @Provides
    @Singleton
    fun provideGroupsInteractor(
        authInteractor: AuthInteractor,
        groupsRepository: GroupsRepository
    ) = GroupsInteractor(authInteractor, groupsRepository)
}