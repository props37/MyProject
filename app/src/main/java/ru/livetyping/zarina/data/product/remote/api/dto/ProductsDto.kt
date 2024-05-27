package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.FiltersDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductItemDto
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.product.CategoryProductInfo
import ru.livetyping.zarina.domain.product.ProductsWithFilters

@Serializable
data class ProductsDto(
    @SerialName("items_count")
    val productTotalCount: Int? = null,

    @SerialName("filters")
    val filters: FiltersDto? = null,

    @SerialName("products")
    val products: List<ProductItemDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toProductsWithFiltersPage(): Page<ProductsWithFilters> {
        checkNotNull(products) { "products is null" }
        checkNotNull(filters) { "filters is null" }
        checkNotNull(productTotalCount) { "productTotalCount is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        val productsWithFilters = ProductsWithFilters(
            products = products.mapNotNull { it.toProductItem() },
            filters = filters.toFilters(),
        )
        return Page(
            data = productsWithFilters,
            paginationInfo = paginationInfo.toPaginationInfo(productTotalCount),
        )
    }

    fun toCategoryProductInfo(categoryId: Category.Id): CategoryProductInfo {
        checkNotNull(filters) { "filters is null" }
        return CategoryProductInfo(
            categoryId = categoryId,
            productCount = checkNotNull(productTotalCount) { "productTotalCount is null" },
            availableFilters = filters.toFilters(),
        )
    }
}
