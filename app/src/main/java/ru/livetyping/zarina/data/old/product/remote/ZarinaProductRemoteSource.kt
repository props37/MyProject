package ru.livetyping.zarina.data.old.product.remote

import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.product.remote.api.IZarinaProductApi
import ru.livetyping.zarina.data.old.product.remote.api.dto.FiltersRequestDto
import ru.livetyping.zarina.data.old.product.remote.api.dto.ProductSortDto
import ru.livetyping.zarina.domain.old.Category
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.ProductSort
import ru.livetyping.zarina.domain.old.TreeFilter

@Factory
class ZarinaProductRemoteSource(
    private val api: IZarinaProductApi,
) : IProductRemoteSource {

    override fun getProduct(id: Product.Id) = flow {
        this.emit(checkNotNull(api.getProduct(id.value).toDomain()))
    }

    override suspend fun getProductPage(
        category: Category,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts> {
        val categoryId =
            filtration?.categories?.items?.firstOrNull { it.isSelected }?.id?.toInt()
                ?: category.id.value
        val response =
            api.getProductPage(
                categoryId = categoryId,
                sort = ProductSortDto.from(sort),
                filters = filtration?.let { FiltersRequestDto.from(it) },
                // adjust page index, because it starts from 1 on the backend
                pageIndex = pageIndex + 1
            )
        val pagination = response.toPagination()
        val products = response.items?.mapNotNull { it.toDomain() }.orEmpty()
        val categoryFilter = category.subcategories.toFilter(categoryId)
        val appliedFiltration = response.filters.toDomain(categoryFilter)
        return Page(pagination, FilteredProducts(products, appliedFiltration))
    }

    override fun getCompleteLook(product: Product) = flow {
        emit(api.getCompleteLook(product.id.value).toDomain())
    }

    override suspend fun getDeliveryAvailability(product: Product) =
        checkNotNull(api.getDeliveryInfo(product.id.value).toDomain())

    override suspend fun getOffers(product: Product, city: City) =
        api.getSizes(product.id.value, city.id.id).mapNotNull { it.toDomain() }

}

private fun List<Category>.toFilter(loadedCategoryId: Int): TreeFilter {
    return TreeFilter(
        items = this.map {
            TreeFilter.Item(
                id = it.id.value.toString(),
                name = it.name,
                isExplicitSelected = loadedCategoryId == it.id.value
            )
        },
        isSingleSelection = true
    )
}
