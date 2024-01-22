package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Color
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductColor

@Serializable
data class ProductColorDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("code")
    val code: String? = null,

    @SerialName("product_id")
    val productId: String? = null,
) {
    fun toProductColor(): ProductColor {
        checkNotNull(id) { "id is null" }
        checkNotNull(code) { "code is null" }
        checkNotNull(productId) { "productId is null" }
        return ProductColor(
            id = ProductColor.Id(id),
            name = checkNotNull(name) { "name is null" },
            color = Color(code),
            productId = Product.Id(productId),
        )
    }
}
