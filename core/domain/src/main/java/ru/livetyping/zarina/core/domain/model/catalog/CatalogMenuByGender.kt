package ru.livetyping.zarina.core.domain.model.catalog

// Marked as stable on config/compose/stability_config.txt
public data class CatalogMenuByGender(
    val women: CatalogMenu,
    val men: CatalogMenu,
)
