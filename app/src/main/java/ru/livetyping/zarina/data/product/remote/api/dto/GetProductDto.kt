package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductDetailsDto
import ru.livetyping.zarina.domain.product.ProductDetails

@Serializable
data class GetProductDto(
    @SerialName("item")
    val product: ProductDetailsDto? = null,

    @SerialName("products")
    val colorVariants: List<ProductDetailsDto>? = null,
) {
    fun toProductDetails(): ProductDetails {
        checkNotNull(product) { "product is null" }
        val colorVariants = colorVariants?.map { it.toProductDetails(colorVariants = null) }
        return product.toProductDetails(colorVariants)
    }
}
