package ru.zarina.zarina.data.product.remote

import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import ru.zarina.zarina.data.product.remote.api.dto.ProductSortDto
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import javax.inject.Inject

class ZarinaProductRemoteSource @Inject constructor(
    private val api: IZarinaProductApi,
) : IProductRemoteSource {

    override suspend fun getProduct(id: Product.Id) =
        checkNotNull(api.getProduct(id.value).toDomain())

    override suspend fun getProductPage(
        category: Category,
        sort: ProductSort,
        pageIndex: Int,
    ): Page<FilteredProducts> {
        // adjust page index, because it starts from 1 on the backend
        val response =
            api.getProductPage(category.id.value, ProductSortDto.from(sort), pageIndex + 1)
        val pagination = response.toPagination()
        val products = response.items?.mapNotNull { it.toDomain() }.orEmpty()
        val filtration = response.filters.toDomain()
        return Page(pagination, FilteredProducts(products, filtration))
    }

    override suspend fun getCompleteLook(product: Product) =
        api.getCompleteLook(product.id.value).toDomain()

    override suspend fun getDeliveryAvailability(product: Product) =
        checkNotNull(api.getDeliveryInfo(product.id.value).toDomain())

    override suspend fun getOffers(product: Product, city: City) =
        api.getSizes(product.id.value, city.id.id).mapNotNull { it.toDomain() }

}
