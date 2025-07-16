package ru.livetyping.zarina.data.order.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.data.order.OrderRepositoryImpl
import ru.livetyping.zarina.data.order.remote.OrderRemoteDataSource
import ru.livetyping.zarina.data.order.remote.OrderRemoteDataSourceImpl
import ru.livetyping.zarina.data.order.remote.api.OrderApi
import ru.livetyping.zarina.data.order.remote.api.OrderApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OrderRepositoryModule {

    @Binds
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    abstract fun bindOrderRemoteDataSource(impl: OrderRemoteDataSourceImpl): OrderRemoteDataSource

    @Binds
    abstract fun bindOrderApi(impl: OrderApiImpl): OrderApi
}
