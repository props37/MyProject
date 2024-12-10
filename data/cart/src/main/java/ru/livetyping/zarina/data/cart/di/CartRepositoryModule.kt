package ru.livetyping.zarina.data.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.data.cart.impl.CartRepositoryImpl
import ru.livetyping.zarina.data.cart.impl.local.CartDataHolder
import ru.livetyping.zarina.data.cart.impl.local.CartDataHolderImpl
import ru.livetyping.zarina.data.cart.impl.local.CartLocalDataSource
import ru.livetyping.zarina.data.cart.impl.local.CartLocalDataSourceImpl
import ru.livetyping.zarina.data.cart.impl.remote.CartRemoteDataSource
import ru.livetyping.zarina.data.cart.impl.remote.CartRemoteDataSourceImpl
import ru.livetyping.zarina.data.cart.impl.remote.api.CartApi
import ru.livetyping.zarina.data.cart.impl.remote.api.CartApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CartRepositoryModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindCartLocalDataSource(impl: CartLocalDataSourceImpl): CartLocalDataSource

    @Binds
    abstract fun bindCartRemoteDataSource(impl: CartRemoteDataSourceImpl): CartRemoteDataSource

    @Binds
    abstract fun bindCartApi(impl: CartApiImpl): CartApi

    @Binds
    @Singleton
    abstract fun bindCartDataHolder(impl: CartDataHolderImpl): CartDataHolder
}
