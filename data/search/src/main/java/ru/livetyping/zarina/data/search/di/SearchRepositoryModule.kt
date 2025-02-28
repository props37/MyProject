package ru.livetyping.zarina.data.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.data.search.impl.SearchRepositoryImpl
import ru.livetyping.zarina.data.search.impl.remote.SearchRemoteDataSource
import ru.livetyping.zarina.data.search.impl.remote.SearchRemoteDataSourceImpl
import ru.livetyping.zarina.data.search.impl.remote.api.SearchApi
import ru.livetyping.zarina.data.search.impl.remote.api.SearchApiImpl

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
    abstract fun bindSearchApi(impl: SearchApiImpl): SearchApi
}
