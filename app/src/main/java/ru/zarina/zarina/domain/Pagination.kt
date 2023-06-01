package ru.zarina.zarina.domain

data class Pagination(
    val currentPageIndex: Int,
    val pageCount: Int,
    val totalItemCount: Int,
)
