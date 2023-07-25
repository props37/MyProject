package ru.zarina.zarina.data.product

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

interface IProductRepository {
    fun getProduct(id: Product.Id): Flow<Product>
    suspend fun getProducts(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts>

    suspend fun getCompleteLook(product: Product): List<Product>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
