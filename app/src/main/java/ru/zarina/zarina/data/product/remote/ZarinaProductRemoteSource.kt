package ru.zarina.zarina.data.product.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import ru.zarina.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductSortDto
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort

@Factory
class ZarinaProductRemoteSource(
    private val api: IZarinaProductApi,
) : IProductRemoteSource {

    override suspend fun getProduct(id: Product.Id) =
        checkNotNull(api.getProduct(id.value).toDomain())

    override suspend fun getProductPage(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts> {
        val response =
            api.getProductPage(
                categoryId = category.id.value,
                sort = ProductSortDto.from(sort),
                filters = filtration?.let { FiltersRequestDto.from(it) },
                // adjust page index, because it starts from 1 on the backend
                pageIndex = pageIndex + 1
            )
        val pagination = response.toPagination()
        val products = response.items?.mapNotNull { it.toDomain() }.orEmpty()
        val appliedFiltration = response.filters.toDomain()
        return Page(pagination, FilteredProducts(products, appliedFiltration))
    }

    override suspend fun getCompleteLook(product: Product) =
        api.getCompleteLook(product.id.value).toDomain()

    override suspend fun getDeliveryAvailability(product: Product) =
        checkNotNull(api.getDeliveryInfo(product.id.value).toDomain())

    override suspend fun getOffers(product: Product, city: City) =
        api.getSizes(product.id.value, city.id.id).mapNotNull { it.toDomain() }

}
