package ru.zarina.zarina.data.product

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.product.remote.IProductRemoteSource
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

@Factory
class ProductRepository(
    private val remote: IProductRemoteSource,
) : IProductRepository {

    override suspend fun getProduct(id: Product.Id) = remote.getProduct(id)

    override suspend fun getProducts(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ) =
        remote.getProductPage(category, sort, filtration, pageIndex)

    override suspend fun getCompleteLook(product: Product) = remote.getCompleteLook(product)

    override suspend fun getDeliveryAvailability(product: Product) =
        remote.getDeliveryAvailability(product)

    override suspend fun getOffers(product: Product, city: City) = remote.getOffers(product, city)

}
