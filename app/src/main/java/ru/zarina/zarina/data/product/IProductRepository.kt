package ru.zarina.zarina.data.product

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product

interface IProductRepository {
    suspend fun getProduct(id: Product.Id): Product
    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
