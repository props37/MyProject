package ru.zarina.zarina.data.product

import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Product

interface IProductRepository {
    suspend fun getProduct(id: String): Product
    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
}
