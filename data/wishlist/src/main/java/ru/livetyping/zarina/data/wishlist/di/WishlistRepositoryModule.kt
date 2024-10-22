package ru.livetyping.zarina.data.wishlist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.data.wishlist.impl.WishlistRepositoryImpl
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistDataHolder
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistDataHolderImpl
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistLocalDataSource
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistLocalDataSourceImpl
import ru.livetyping.zarina.data.wishlist.impl.remote.WishlistRemoteDataSource
import ru.livetyping.zarina.data.wishlist.impl.remote.WishlistRemoteDataSourceImpl
import ru.livetyping.zarina.data.wishlist.impl.remote.api.WishlistApi
import ru.livetyping.zarina.data.wishlist.impl.remote.api.WishlistApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WishlistRepositoryModule {

    @Binds
    abstract fun bindWishlistRepository(
        impl: WishlistRepositoryImpl,
    ): WishlistRepository

    @Binds
    abstract fun bindWishlistRemoteDataSource(
        impl: WishlistRemoteDataSourceImpl,
    ): WishlistRemoteDataSource

    @Binds
    abstract fun bindWishlistApi(impl: WishlistApiImpl): WishlistApi

    @Binds
    abstract fun bindWishlistLocalDataSource(
        impl: WishlistLocalDataSourceImpl,
    ): WishlistLocalDataSource

    @Binds
    abstract fun bindWishlistDataHolder(
        impl: WishlistDataHolderImpl,
    ): WishlistDataHolder
}
