package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.domain.Product

interface IProductRemoteSource {
    suspend fun getProduct(id: String): Product
    suspend fun getCompleteLook(product: Product): List<Product>
}
