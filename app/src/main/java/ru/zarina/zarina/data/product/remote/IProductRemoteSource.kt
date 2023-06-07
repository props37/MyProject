package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

interface IProductRemoteSource {
    suspend fun getProduct(id: Product.Id): Product
    suspend fun getProductPage(
        category: Category,
        sort: ProductSort,
        pageIndex: Int,
    ): Page<FilteredProducts>

    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
