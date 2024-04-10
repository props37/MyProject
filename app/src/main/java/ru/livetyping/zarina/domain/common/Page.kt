package ru.livetyping.zarina.domain.common

data class Page<T>(
    val data: T,
    val paginationInfo: PaginationInfo,
)
