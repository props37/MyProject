package ru.livetyping.zarina.data.content.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenu

@Serializable
internal data class CatalogMenuDto(
    @SerialName("top")
    val top: List<CatalogMenuItemDto>? = null,

    @SerialName("middle")
    val middle: List<CatalogMenuItemDto>? = null,

    @SerialName("bottom")
    val bottom: List<CatalogMenuItemDto>? = null,
) {
    fun toCatalogMenu(): CatalogMenu {
        val top = top
            ?.mapNotNull { it.toCatalogMenuItem() }
            ?.takeIf { it.isNotEmpty() }
        val middle = middle
            ?.mapNotNull { it.toCatalogMenuItem() }
            ?.takeIf { it.isNotEmpty() }
        val bottom = bottom
            ?.mapNotNull { it.toCatalogMenuItem() }
            ?.takeIf { it.isNotEmpty() }
        return CatalogMenu(top, middle, bottom)
    }
}
