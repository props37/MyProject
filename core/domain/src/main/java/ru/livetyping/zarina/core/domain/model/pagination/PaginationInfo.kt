package ru.livetyping.zarina.core.domain.model.pagination

// Marked as stable on config/compose/stability_config.txt
public data class PaginationInfo(
    val currentPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val itemTotalCount: Int,
)
