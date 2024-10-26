package ru.livetyping.zarina.data.content.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.ContentRepositoryImpl
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSourceImpl
import ru.livetyping.zarina.data.content.impl.local.gender.ContentGenderDataHolder
import ru.livetyping.zarina.data.content.impl.local.gender.ContentGenderDataHolderImpl
import ru.livetyping.zarina.data.content.impl.remote.ContentRemoteDataSource
import ru.livetyping.zarina.data.content.impl.remote.ContentRemoteDataSourceImpl
import ru.livetyping.zarina.data.content.impl.remote.api.ContentApi
import ru.livetyping.zarina.data.content.impl.remote.api.ContentApiImpl
import ru.livetyping.zarina.feature.onboarding.domain.repository.OnboardingRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ContentRepositoryModule {

    @Binds
    abstract fun bindContentRepository(
        impl: ContentRepositoryImpl,
    ): ContentRepository

    @Binds
    abstract fun bindOnboardingRepository(
        impl: ContentRepositoryImpl,
    ): OnboardingRepository

    @Binds
    abstract fun bindContentLocalDataSource(
        impl: ContentLocalDataSourceImpl,
    ): ContentLocalDataSource

    @Binds
    abstract fun bindContentGenderDataHolder(
        impl: ContentGenderDataHolderImpl,
    ): ContentGenderDataHolder

    @Binds
    abstract fun bindContentRemoteDataSource(
        impl: ContentRemoteDataSourceImpl,
    ): ContentRemoteDataSource

    @Binds
    abstract fun bindContentApi(impl: ContentApiImpl): ContentApi
}
