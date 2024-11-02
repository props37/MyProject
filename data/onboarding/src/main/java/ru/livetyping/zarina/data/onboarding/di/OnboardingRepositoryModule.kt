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

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OnboardingRepositoryModule {

    @Binds
    abstract fun bindsOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    abstract fun bindsOnboardingLocalDataSource(
        impl: OnboardingLocalDataSourceImpl,
    ): OnboardingLocalDataSource

    @Binds
    abstract fun bindsOnboardingDataHolder(
        impl: OnboardingDataHolderImpl,
    ): OnboardingDataHolder
}
