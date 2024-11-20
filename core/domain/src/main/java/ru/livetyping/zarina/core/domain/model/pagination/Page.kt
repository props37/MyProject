package ru.livetyping.zarina.core.domain.model.pagination

public data class Page<T>(
    val data: T,
    val paginationInfo: PaginationInfo,
)
