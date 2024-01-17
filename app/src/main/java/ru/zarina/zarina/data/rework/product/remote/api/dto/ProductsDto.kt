package ru.zarina.zarina.data.rework.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.ProductDto

@Serializable
data class ProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("products")
    val products: List<ProductDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfo? = null,
) {
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
