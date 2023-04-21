package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.domain.Product

interface IProductRemoteSource {
    suspend fun getProduct(id: String): Product
}
