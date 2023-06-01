package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import javax.inject.Inject

class ZarinaProductRemoteSource @Inject constructor(
    private val api: IZarinaProductApi,
) : IProductRemoteSource {

    override suspend fun getProduct(id: Product.Id) =
        checkNotNull(api.getProduct(id.value).toDomain())

    override suspend fun getProductPage(category: Category, pageIndex: Int): Page<List<Product>> {
        val response = api.getProductPage(category.id.value, pageIndex)
        val pagination = response.toPagination()
        val products = response.items?.mapNotNull { it.toDomain() }.orEmpty()
        return Page(pagination, products)
    }

    override suspend fun getCompleteLook(product: Product) =
        api.getCompleteLook(product.id.value).toDomain()

    override suspend fun getDeliveryAvailability(product: Product) =
        checkNotNull(api.getDeliveryInfo(product.id.value).toDomain())

    override suspend fun getOffers(product: Product, city: City) =
        api.getSizes(product.id.value, city.id.id).mapNotNull { it.toDomain() }

}
