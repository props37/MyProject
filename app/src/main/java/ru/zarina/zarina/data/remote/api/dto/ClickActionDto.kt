package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.rework.common.ClickAction
import timber.log.Timber

@Serializable
data class ClickActionDto(
    @SerialName("type")
    val type: String? = null,

    @SerialName("payload")
    val payload: Payload? = null,
) {
    @Serializable
    data class Payload(
        @SerialName("category_id")
        val categoryId: Long? = null,
    )

    fun toClickAction(): ClickAction? {
        if (type == null || payload == null) return null
        return when (type) {
            TYPE_PRODUCT_LIST -> toClickActionProducts()
            else -> {
                Timber.w("Unknown type $type")
                null
            }
        }
    }

    private fun toClickActionProducts(): ClickAction.Products? {
        if (payload?.categoryId == null) return null
        return ClickAction.Products(
            categoryId = Category.Id(payload.categoryId),
        )
    }

    companion object {
        private const val TYPE_PRODUCT_LIST = "products-list"
    }
}
