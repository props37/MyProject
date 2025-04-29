package ru.livetyping.zarina.data.content.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.network.zarina.dto.ClickActionDto
import timber.log.Timber

@Serializable
internal data class CatalogMenuItemDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("sign")
    val sign: String? = null,

    @SerialName("color")
    val color: String? = null,

    @SerialName("click")
    val click: ClickActionDto? = null,

    @SerialName("childs")
    val children: List<CatalogMenuItemDto>? = null,
) {
    fun toCatalogMenuItem(): CatalogMenuItem? {
        val clickAction = click?.toClickAction()
        return if (id != null && title != null && clickAction != null) {
            val children = children
                ?.mapNotNull { it.toCatalogMenuItem() }
                ?.takeIf { it.isNotEmpty() }
            CatalogMenuItem(
                id = CatalogMenuItem.Id(id.toString()),
                title = title,
                label = sign,
                color = color?.let { Color(it) },
                clickAction = clickAction,
                children = children,
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to CatalogMenuItem")
            null
        }
    }

    private companion object {
        private const val TAG = "CatalogMenuItemDto"
    }
}
