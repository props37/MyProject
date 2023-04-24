package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Color
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class ColorDto(
    @SerialName("id")
    val id: String?,
    @SerialName("title")
    val name: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("product_id")
    val productId: String?,
    // This field is always `true` according to the backend developer
    @SerialName("product_is_available")
    val productIsAvailable: Boolean?,
    @SerialName("is_current")
    val isCurrent: Boolean?,
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
            isNotNull(id, "id")
            && isNotNull(name, "title")
            && isNotNull(code, "code")
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
            isNotNull(productId, "product_id")
            && isNotNull(isCurrent, "is_current")
        )
            Product.Variant(
                id = productId,
                isCurrent = isCurrent,
            )
        else
            null
    }

}
