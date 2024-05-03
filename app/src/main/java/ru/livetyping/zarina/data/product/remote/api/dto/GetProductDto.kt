package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductDetailsDto

@Serializable
data class GetProductDto(
    @SerialName("item")
    val product: ProductDetailsDto? = null,

    @SerialName("products")
    val otherColors: List<ProductDetailsDto>? = null,
)
