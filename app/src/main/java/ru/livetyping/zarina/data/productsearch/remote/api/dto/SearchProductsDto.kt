package ru.livetyping.zarina.data.productsearch.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductItemDto
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult

@Serializable
data class SearchProductsDto(
    @SerialName("products")
    val products: List<ProductItemDto>? = null,

    @SerialName("totalHits")
    val productTotalCount: Int? = null,

    @SerialName("pageCount")
    val pageSize: Int? = null,

    @SerialName("offset")
    val offset: Int? = null,
) {
    fun toProductSearchResult(): ProductSearchResult {
        checkNotNull(products) { "products is null" }
        checkNotNull(productTotalCount) { "productTotalCount is null" }
        checkNotNull(offset) { "offset is null" }
        val products = products.mapNotNull { it.toProductItem() }
        return ProductSearchResult(
            products = products,
            productTotalCount = productTotalCount,
            offset = offset,
        )
    }
}
