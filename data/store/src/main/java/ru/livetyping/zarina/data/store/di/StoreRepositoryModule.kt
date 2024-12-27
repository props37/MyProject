package ru.livetyping.zarina.data.store.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.data.store.impl.StoreRepositoryImpl
import ru.livetyping.zarina.data.store.impl.local.StoreLocalDataSource
import ru.livetyping.zarina.data.store.impl.local.StoreLocalDataSourceImpl
import ru.livetyping.zarina.data.store.impl.remote.StoreRemoteDataSource
import ru.livetyping.zarina.data.store.impl.remote.StoreRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class StoreRepositoryModule {

    @Binds
    abstract fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository

    @Binds
    abstract fun bindStoreRemoteDataSource(impl: StoreRemoteDataSourceImpl): StoreRemoteDataSource

    @Binds
    abstract fun bindStoreLocalDataSource(impl: StoreLocalDataSourceImpl): StoreLocalDataSource
}
