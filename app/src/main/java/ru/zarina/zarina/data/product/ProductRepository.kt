package ru.zarina.zarina.data.product

import ru.zarina.zarina.data.product.remote.IProductRemoteSource
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remote: IProductRemoteSource,
) : IProductRepository {

    override suspend fun getProduct(id: Product.Id) = remote.getProduct(id)

    override suspend fun getProducts(category: Category, sort: ProductSort, pageIndex: Int) =
        remote.getProductPage(category, sort, pageIndex)

    override suspend fun getCompleteLook(product: Product) = remote.getCompleteLook(product)

    override suspend fun getDeliveryAvailability(product: Product) =
        remote.getDeliveryAvailability(product)

    override suspend fun getOffers(product: Product, city: City) = remote.getOffers(product, city)

}
