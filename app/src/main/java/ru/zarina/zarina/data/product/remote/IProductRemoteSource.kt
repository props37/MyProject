package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Product

interface IProductRemoteSource {
    suspend fun getProduct(id: Product.Id): Product
    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
}
