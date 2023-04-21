package ru.zarina.zarina.data.product

import ru.zarina.zarina.domain.Product

interface IProductRepository {
    suspend fun getProduct(id: String): Product
}
