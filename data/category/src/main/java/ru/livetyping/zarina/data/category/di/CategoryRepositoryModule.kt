package ru.livetyping.zarina.data.category.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.data.category.impl.CategoryRepositoryImpl
import ru.livetyping.zarina.data.category.impl.local.CategoryDataHolder
import ru.livetyping.zarina.data.category.impl.local.CategoryDataHolderImpl
import ru.livetyping.zarina.data.category.impl.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.impl.local.CategoryLocalDataSourceImpl
import ru.livetyping.zarina.data.category.impl.remote.CategoryRemoteDataSource
import ru.livetyping.zarina.data.category.impl.remote.CategoryRemoteDataSourceImpl
import ru.livetyping.zarina.data.category.impl.remote.api.CategoryApi
import ru.livetyping.zarina.data.category.impl.remote.api.CategoryApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CategoryRepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindCategoryRemoteDataSource(
        impl: CategoryRemoteDataSourceImpl,
    ): CategoryRemoteDataSource

    @Binds
    abstract fun bindCategoryApi(impl: CategoryApiImpl): CategoryApi

    @Binds
    abstract fun bindCategoryLocalDataSource(
        impl: CategoryLocalDataSourceImpl,
    ): CategoryLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryDataHolder(impl: CategoryDataHolderImpl): CategoryDataHolder
}
