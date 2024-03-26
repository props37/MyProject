package ru.zarina.zarina.data.old.product.remote

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.Category
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.domain.old.DeliveryAvailability
import ru.zarina.zarina.domain.old.FilteredProducts
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.Offer
import ru.zarina.zarina.domain.old.Page
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.ProductSort

interface IProductRemoteSource {
    fun getProduct(id: Product.Id): Flow<Product>
    suspend fun getProductPage(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts>

    fun getCompleteLook(product: Product): Flow<List<Product>>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
