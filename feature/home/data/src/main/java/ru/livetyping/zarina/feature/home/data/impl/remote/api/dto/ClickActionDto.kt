package ru.livetyping.zarina.feature.home.data.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.feature.home.domain.model.ClickAction
import timber.log.Timber

@Serializable
internal data class ClickActionDto(
    @SerialName("type")
    val type: String? = null,

    @SerialName("payload")
    val payload: Payload? = null,
) {
    @Serializable
    data class Payload(
        @SerialName("category_id")
        val categoryId: Long? = null,

        @SerialName("link")
        val link: String? = null,
    )

    fun toClickAction(): ClickAction? {
        if (type == null || payload == null) return null
        return when (type) {
            TYPE_PRODUCT_LIST -> toClickActionOpenProductList()
            TYPE_LOOKBOOK -> toClickActionOpenUrl()
            TYPE_WEB_VIEW -> toClickActionOpenUrl()
            else -> {
                Timber.tag(TAG).w("Unknown type $type")
                null
            }
        }
    }

    private fun toClickActionOpenProductList(): ClickAction.OpenProductList? {
        if (payload?.categoryId == null) return null
        return ClickAction.OpenProductList(
            categoryId = Category.Id(payload.categoryId.toString()),
        )
    }

    private fun toClickActionOpenUrl(): ClickAction.OpenUrl? {
        if (payload?.link == null) return null
        return ClickAction.OpenUrl(url = Url.create(payload.link))
    }

    companion object {
        private const val TYPE_PRODUCT_LIST = "products-list"
        private const val TYPE_LOOKBOOK = "lookbook"
        private const val TYPE_WEB_VIEW = "web-view"

        private const val TAG = "ClickActionDto"
    }
}
