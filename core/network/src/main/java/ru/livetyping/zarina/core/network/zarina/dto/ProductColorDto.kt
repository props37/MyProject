package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import timber.log.Timber

@Serializable
public data class ProductColorDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("code")
    val code: String? = null,

    @SerialName("product_id")
    val productId: String? = null,
) {
    public fun toProductColor(): ProductColor? {
        return if (id != null && name != null && code != null && productId != null) {
            ProductColor(
                id = ProductColor.Id(id),
                name = name,
                color = Color(code),
                productId = Product.Id(productId),
            )
        } else {
            Timber.tag(TAG).e("Drop ProductColorDto because its id, name, code or productId is null")
            null
        }
    }

    private companion object {
        private const val TAG = "ProductColorDto"
    }
}
