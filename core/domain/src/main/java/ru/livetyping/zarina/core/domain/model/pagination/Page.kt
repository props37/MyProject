package ru.livetyping.zarina.core.domain.model.pagination

// Marked as stable on config/compose/stability_config.txt
public data class Page<T>(
    val data: T,
    val paginationInfo: PaginationInfo,
)
