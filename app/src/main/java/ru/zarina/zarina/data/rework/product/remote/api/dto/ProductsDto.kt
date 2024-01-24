package ru.zarina.zarina.data.rework.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.FiltersDto
import ru.zarina.zarina.data.rework.common.remote.api.dto.ProductDto
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.common.PaginationInfo as DomainPaginationInfo

@Serializable
data class ProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("filters")
    val filters: FiltersDto? = null,

    @SerialName("products")
    val products: List<ProductDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfo? = null,
) {
    fun toProductPage(): Page<List<Product>> {
        checkNotNull(products) { "products is null" }
        checkNotNull(filters) { "filters is null" }
        return Page(
            data = products.map { it.toProduct() },
            paginationInfo = getPaginationInfo(),
        )
    }

    private fun getPaginationInfo(): DomainPaginationInfo {
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        return DomainPaginationInfo(
            currentPage = checkNotNull(paginationInfo.currentPage) { "currentPage is null" },
            pageCount = checkNotNull(paginationInfo.pageCount) { "pageCount is null" },
            pageSize = checkNotNull(paginationInfo.pageSize) { "pageSize is null" },
            itemCount = checkNotNull(itemCount) { "itemCount is null" },
        )
    }

    @Serializable
    data class PaginationInfo(
        @SerialName("current_page")
        val currentPage: Int? = null,

        @SerialName("total_pages")
        val pageCount: Int? = null,

        @SerialName("page_size")
        val pageSize: Int? = null,
    )
}
