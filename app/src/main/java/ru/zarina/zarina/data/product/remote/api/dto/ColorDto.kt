package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Color
import ru.zarina.zarina.domain.Product

@Serializable
data class ColorDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val name: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("product_id")
    val productId: String? = null,
    // This field is always `true` according to the backend developer
    @SerialName("product_is_available")
    val productIsAvailable: Boolean? = null,
    @SerialName("is_current")
    val isCurrent: Boolean? = null,
) {
    fun toDomain(): Pair<Color, Product.Variant>? {
        val color = toColor()
        val variant = toProductVariant()
        return if (color != null && variant != null)
            color to variant
        else
            null
    }

    private fun toColor(): Color? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "title")
            && ApiContract.isNotNull(code, "code")
        )
            Color(
                id = id,
                name = name,
                code = Color.Code(code),
            )
        else
            null
    }

    private fun toProductVariant(): Product.Variant? {
        return if (
            ApiContract.isNotNull(productId, "product_id")
            && ApiContract.isNotNull(isCurrent, "is_current")
        )
            Product.Variant(
                id = productId,
                isCurrent = isCurrent,
            )
        else
            null
    }

}
