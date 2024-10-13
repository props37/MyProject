package ru.livetyping.zarina.feature.home.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.feature.home.data.impl.remote.HomeContentRemoteDataSource
import ru.livetyping.zarina.feature.home.data.impl.remote.HomeContentRemoteDataSourceImpl
import ru.livetyping.zarina.feature.home.data.impl.remote.api.HomeContentApi
import ru.livetyping.zarina.feature.home.data.impl.remote.api.HomeContentApiImpl
import ru.livetyping.zarina.feature.home.data.repository.HomeContentRepositoryImpl
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HomeRepositoryModule {

    @Binds
    abstract fun bindHomeContentRepository(
        impl: HomeContentRepositoryImpl,
    ): HomeContentRepository

    @Binds
    abstract fun bindHomeContentRemoteDataSource(
        impl: HomeContentRemoteDataSourceImpl,
    ): HomeContentRemoteDataSource

    @Binds
    abstract fun bindHomeContentApi(
        impl: HomeContentApiImpl,
    ): HomeContentApi
}
