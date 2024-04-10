package ru.livetyping.zarina.data.favorite.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductDto
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.product.Product

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
