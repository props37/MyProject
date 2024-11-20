package ru.livetyping.zarina.core.domain.model.pagination

public data class PaginationInfo(
    val currentPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val itemTotalCount: Int,
)
