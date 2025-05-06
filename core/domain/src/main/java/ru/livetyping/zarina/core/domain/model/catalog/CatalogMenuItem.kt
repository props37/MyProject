package ru.livetyping.zarina.core.domain.model.catalog

import ru.livetyping.zarina.core.domain.model.common.ClickAction
import ru.livetyping.zarina.core.domain.model.common.Color

// Marked as stable on config/compose/stability_config.txt
public data class CatalogMenuItem(
    val id: Id,
    val title: String,
    val label: String?,
    val color: Color?,
    val clickAction: ClickAction,
    val children: List<CatalogMenuItem>?,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    public val isExpandable: Boolean get() = !children.isNullOrEmpty()
}
