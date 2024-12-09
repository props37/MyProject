package ru.livetyping.zarina.data.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.data.cart.impl.CartRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CartRepositoryModule {

    @Binds
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}
