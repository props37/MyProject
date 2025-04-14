package ru.livetyping.zarina.data.checkout.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.data.checkout.impl.CheckoutRepositoryImpl
import ru.livetyping.zarina.data.checkout.impl.local.CheckoutLocalDataSource
import ru.livetyping.zarina.data.checkout.impl.local.CheckoutLocalDataSourceImpl
import ru.livetyping.zarina.data.checkout.impl.local.CompletedPaymentDataHolder
import ru.livetyping.zarina.data.checkout.impl.local.CompletedPaymentDataHolderImpl
import ru.livetyping.zarina.data.checkout.impl.remote.CheckoutRemoteDataSource
import ru.livetyping.zarina.data.checkout.impl.remote.CheckoutRemoteDataSourceImpl
import ru.livetyping.zarina.data.checkout.impl.remote.api.CheckoutApi
import ru.livetyping.zarina.data.checkout.impl.remote.api.CheckoutApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CheckoutRepositoryModule {

    @Binds
    abstract fun bindCheckoutRepostory(impl: CheckoutRepositoryImpl): CheckoutRepository

    @Binds
    abstract fun bindCheckoutRemoteDataSource(
        impl: CheckoutRemoteDataSourceImpl,
    ): CheckoutRemoteDataSource

    @Binds
    abstract fun bindCheckoutApi(impl: CheckoutApiImpl) : CheckoutApi

    @Binds
    abstract fun bindCheckoutLocalDataSource(
        impl: CheckoutLocalDataSourceImpl,
    ) : CheckoutLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCompletedPaymentDataHolder(
        impl: CompletedPaymentDataHolderImpl,
    ) : CompletedPaymentDataHolder
}
