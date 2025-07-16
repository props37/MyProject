package ru.livetyping.zarina.data.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.data.search.SearchRepositoryImpl
import ru.livetyping.zarina.data.search.local.SearchLocalDataSource
import ru.livetyping.zarina.data.search.local.SearchLocalDataSourceImpl
import ru.livetyping.zarina.data.search.remote.SearchRemoteDataSource
import ru.livetyping.zarina.data.search.remote.SearchRemoteDataSourceImpl
import ru.livetyping.zarina.data.search.remote.api.SearchApi
import ru.livetyping.zarina.data.search.remote.api.SearchApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SearchRepositoryModule {

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindSearchRemoteDataSource(
        impl: SearchRemoteDataSourceImpl,
    ): SearchRemoteDataSource

    @Binds
    abstract fun bindSearchLocalDataSource(
        impl: SearchLocalDataSourceImpl,
    ): SearchLocalDataSource

    @Binds
    abstract fun bindSearchApi(impl: SearchApiImpl): SearchApi
}
