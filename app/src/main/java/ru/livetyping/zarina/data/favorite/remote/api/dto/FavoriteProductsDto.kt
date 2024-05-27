package ru.livetyping.zarina.data.favorite.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductItemDto
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.product.ProductItem

@Serializable
data class FavoriteProductsDto(
    @SerialName("items_count")
    val productTotalCount: Int? = null,

    @SerialName("items")
    val products: List<ProductItemDto>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toProductPage(): Page<List<ProductItem>> {
        checkNotNull(products) { "products is null" }
        checkNotNull(productTotalCount) { "itemsCount is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        return Page(
            data = products.mapNotNull { it.toProductItem() },
            paginationInfo = paginationInfo.toPaginationInfo(productTotalCount),
        )
    }
}
