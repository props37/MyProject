package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.common.remote.api.dto.FiltersDto
import ru.zarina.zarina.data.common.remote.api.dto.PaginationInfoDto
import ru.zarina.zarina.data.common.remote.api.dto.ProductDto
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.common.Page
import ru.zarina.zarina.domain.product.CategoryProductInfo
import ru.zarina.zarina.domain.product.ProductsWithFilters

@Serializable
data class ProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("filters")
    val filters: FiltersDto? = null,

    @SerialName("products")
    val products: List<ProductDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toProductsWithFiltersPage(): Page<ProductsWithFilters> {
        checkNotNull(products) { "products is null" }
        checkNotNull(filters) { "filters is null" }
        checkNotNull(itemCount) { "itemCount is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        val productsWithFilters = ProductsWithFilters(
            products = products.mapNotNull { it.toProduct() },
            filters = filters.toFilters(),
        )
        return Page(
            data = productsWithFilters,
            paginationInfo = paginationInfo.toPaginationInfo(itemCount),
        )
    }

    fun toCategoryProductInfo(categoryId: Category.Id): CategoryProductInfo {
        checkNotNull(filters) { "filters is null" }
        return CategoryProductInfo(
            categoryId = categoryId,
            productCount = checkNotNull(itemCount) { "itemCount is null" },
            availableFilters = filters.toFilters(),
        )
    }
}
