package ru.zarina.zarina.data.rework.product

import ru.zarina.zarina.data.rework.product.remote.ProductRemoteDataSource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource,
)
