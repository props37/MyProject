package ru.livetyping.zarina.data.wishlist.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.core.network.zarina.dto.ProductShortDto

@Serializable
public data class WishlistProductsDto(
    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("items")
    val items: List<ProductShortDto>? = null,

    @SerialName("pagination")
    val pagination: PaginationInfoDto? = null,
) {
    public fun toProductPage(): Page<List<ProductShort>> {
        checkPropertyNotNull(items) { ::items }
        checkPropertyNotNull(itemCount) { ::itemCount }
        checkPropertyNotNull(pagination) { ::pagination }
        val data = items.mapNotNull { it.toProductShort() }
        val paginationInfo = pagination.toPaginationInfo(itemCount)
        return Page(
            data = data,
            paginationInfo = paginationInfo,
        )
    }
}
