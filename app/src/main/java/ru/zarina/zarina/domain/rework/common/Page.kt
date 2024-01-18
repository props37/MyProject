package ru.zarina.zarina.domain.rework.common

data class Page<T>(
    val data: List<T>,
    val paginationInfo: PaginationInfo,
)
