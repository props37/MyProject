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

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ContentRepositoryModule {

    @Binds
    abstract fun bindContentRepository(
        impl: ContentRepositoryImpl,
    ): ContentRepository

    @Binds
    abstract fun bindContentLocalDataSource(
        impl: ContentLocalDataSourceImpl,
    ): ContentLocalDataSource

    @Binds
    abstract fun bindContentGenderDataHolder(
        impl: ContentGenderDataHolderImpl,
    ): ContentGenderDataHolder
}
