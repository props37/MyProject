package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.core.network.zarina.dto.ProductShortDto

@Serializable
internal data class ProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("filters")
    val filters: FiltersDto? = null,

    @SerialName("products")
    val products: List<ProductShortDto>? = null,

    @SerialName("pagination")
    val pagination: PaginationInfoDto? = null,
) {
    fun toProductsWithFiltersPage(): Page<ProductsWithFilters> {
        checkPropertyNotNull(products) { ::products }
        checkPropertyNotNull(filters) { ::filters }
        checkPropertyNotNull(itemCount) { ::itemCount }
        checkPropertyNotNull(pagination) { ::pagination }
        val productsWithFilters = ProductsWithFilters(
            products = products.mapNotNull { it.toProductShort() },
            filters = filters.toFilters(),
        )
        return Page(
            data = productsWithFilters,
            paginationInfo = pagination.toPaginationInfo(itemCount),
        )
    }

    fun toCategoryInfo(categoryId: Category.Id): CategoryInfo {
        checkPropertyNotNull(filters) { ::filters }
        return CategoryInfo(
            categoryId = categoryId,
            productCount = checkPropertyNotNull(itemCount) { ::itemCount },
            availableFilters = filters.toFilters(),
        )
    }
}