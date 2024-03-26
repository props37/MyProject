package ru.zarina.zarina.domain.old

data class Page<T>(
    val pagination: Pagination,
    val value: T,
)
