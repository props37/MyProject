package ru.zarina.zarina.domain

data class Page<T>(
    val pagination: Pagination,
    val value: T,
)
