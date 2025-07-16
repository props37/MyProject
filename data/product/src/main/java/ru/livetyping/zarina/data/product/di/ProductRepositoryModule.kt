package ru.livetyping.zarina.data.product.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.data.product.ProductRepositoryImpl
import ru.livetyping.zarina.data.product.remote.ProductRemoteDataSource
import ru.livetyping.zarina.data.product.remote.ProductRemoteDataSourceImpl
import ru.livetyping.zarina.data.product.remote.api.ProductApi
import ru.livetyping.zarina.data.product.remote.api.ProductApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProductRepositoryModule {

    @Binds
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindProductRemoteDataSource(
        impl: ProductRemoteDataSourceImpl,
    ): ProductRemoteDataSource

    @Binds
    abstract fun bindProductApi(impl: ProductApiImpl): ProductApi
}
