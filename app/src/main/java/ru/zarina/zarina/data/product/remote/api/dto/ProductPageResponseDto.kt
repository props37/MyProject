package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto

@Serializable
data class ProductPageResponseDto(
    @SerialName("items_count")
    val itemCount: Int? = null,
    @SerialName("pagination")
    val pagination: PaginationDto? = null,
    @SerialName("items")
    val items: List<ProductDto>? = null,
)
