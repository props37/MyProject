package ru.livetyping.zarina.domain.common

data class PaginationInfo(
    val currentPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val itemTotalCount: Int,
)
