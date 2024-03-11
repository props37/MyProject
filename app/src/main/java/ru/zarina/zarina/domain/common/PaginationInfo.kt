package ru.zarina.zarina.domain.common

data class PaginationInfo(
    val currentPage: Int,
    val pageCount: Int,
    val pageSize: Int,
    val itemCount: Int,
)
