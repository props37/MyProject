package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.common.Color
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductColor
import timber.log.Timber

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
    fun toProductColor(): ProductColor? {
        return if (id != null && name != null && code != null && productId != null) {
            ProductColor(
                id = ProductColor.Id(id),
                name = name,
                color = Color(code),
                productId = Product.Id(productId),
            )
        } else {
            Timber.e("Drop ProductColor because its ID, name, color code or product ID is null")
            null
        }
    }
}
