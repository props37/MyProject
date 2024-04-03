package ru.livetyping.zarina.data.old.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Category
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.DeliveryAvailability
import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.ProductSort

interface IProductRepository {
    fun getProduct(id: Product.Id): Flow<Product>
    suspend fun getProducts(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts>

    fun getCompleteLook(product: Product): Flow<List<Product>>
    suspend fun getDeliveryAvailability(product: Product): DeliveryAvailability
    suspend fun getOffers(product: Product, city: City): List<Offer>
}
