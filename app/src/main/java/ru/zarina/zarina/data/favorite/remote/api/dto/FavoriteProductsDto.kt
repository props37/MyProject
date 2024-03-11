package ru.zarina.zarina.data.favorite.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.remote.api.dto.PaginationInfoDto
import ru.zarina.zarina.data.remote.api.dto.ProductDto
import ru.zarina.zarina.domain.common.Page
import ru.zarina.zarina.domain.product.Product

@Serializable
data class FavoriteProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("items")
    val products: List<ProductDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toProductPage(): Page<List<Product>> {
        checkNotNull(products) { "products is null" }
        checkNotNull(itemCount) { "itemsCount is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        return Page(
            data = products.mapNotNull { it.toProduct() },
            paginationInfo = paginationInfo.toPaginationInfo(itemCount),
        )
    }
}
