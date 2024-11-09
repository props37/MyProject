package ru.livetyping.zarina.data.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.data.onboarding.impl.OnboardingRepositoryImpl
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingDataHolder
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingDataHolderImpl
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingLocalDataSource
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingLocalDataSourceImpl
import ru.livetyping.zarina.data.onboarding.impl.remote.OnboardingRemoteDataSource
import ru.livetyping.zarina.data.onboarding.impl.remote.OnboardingRemoteDataSourceImpl
import ru.livetyping.zarina.data.onboarding.impl.remote.api.OnboardingApi
import ru.livetyping.zarina.data.onboarding.impl.remote.api.OnboardingApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OnboardingRepositoryModule {

    @Binds
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    abstract fun bindOnboardingLocalDataSource(
        impl: OnboardingLocalDataSourceImpl,
    ): OnboardingLocalDataSource

    @Binds
    abstract fun bindOnboardingDataHolder(
        impl: OnboardingDataHolderImpl,
    ): OnboardingDataHolder

    @Binds
    abstract fun bindOnboardingRemoteDataSource(
        impl: OnboardingRemoteDataSourceImpl,
    ): OnboardingRemoteDataSource

    @Binds
    abstract fun bindOnboardingApi(impl: OnboardingApiImpl): OnboardingApi
}
