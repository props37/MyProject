package ru.livetyping.zarina.data.content.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.ContentRepositoryImpl
import ru.livetyping.zarina.data.content.local.ContentLocalDataSource
import ru.livetyping.zarina.data.content.local.ContentLocalDataSourceImpl
import ru.livetyping.zarina.data.content.local.catalog.CatalogDataHolder
import ru.livetyping.zarina.data.content.local.catalog.CatalogDataHolderImpl
import ru.livetyping.zarina.data.content.remote.ContentRemoteDataSource
import ru.livetyping.zarina.data.content.remote.ContentRemoteDataSourceImpl
import ru.livetyping.zarina.data.content.remote.api.ContentApi
import ru.livetyping.zarina.data.content.remote.api.ContentApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ContentRepositoryModule {

    @Binds
    abstract fun bindContentRepository(impl: ContentRepositoryImpl): ContentRepository

    @Binds
    abstract fun bindContentLocalDataSource(
        impl: ContentLocalDataSourceImpl,
    ): ContentLocalDataSource

    @Binds
    abstract fun bindContentRemoteDataSource(
        impl: ContentRemoteDataSourceImpl,
    ): ContentRemoteDataSource

    @Binds
    abstract fun bindContentApi(impl: ContentApiImpl): ContentApi

    @Binds
    @Singleton
    abstract fun bindCatalogDataHolder(impl: CatalogDataHolderImpl): CatalogDataHolder
}
