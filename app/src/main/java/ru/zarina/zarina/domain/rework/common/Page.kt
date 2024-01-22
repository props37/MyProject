package ru.zarina.zarina.domain.rework.common

data class Page<T>(
    val data: T,
    val paginationInfo: PaginationInfo,
)
