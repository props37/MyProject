package ru.zarina.zarina.data.rework.product.remote

import ru.zarina.zarina.data.rework.product.remote.api.ProductApi
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: ProductApi,
)
