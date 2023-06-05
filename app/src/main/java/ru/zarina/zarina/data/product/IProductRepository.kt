package ru.zarina.zarina.data.product

import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

interface IProductRepository {
    suspend fun getProduct(id: Product.Id): Product
    suspend fun getProducts(
        category: Category,
        sort: ProductSort,
        pageIndex: Int,
    ): Page<List<Product>>

    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
