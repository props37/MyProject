package ru.livetyping.zarina.core.domain.model.catalog

// Marked as stable on config/compose/stability_config.txt
public data class CatalogMenu(
    val top: List<CatalogMenuItem>?,
    val middle: List<CatalogMenuItem>?,
    val bottom: List<CatalogMenuItem>?,
)
