package ru.zarina.zarina.domain

data class Pagination(
    val currentPageIndex: Int,
    val pageCount: Int,
    val totalItemCount: Int,
) {

    val nextPageIndex: Int?
        get() = if (currentPageIndex == pageCount - 1) null else currentPageIndex + 1

    val previousPageIndex: Int?
        get() = if (currentPageIndex == 0) null else currentPageIndex - 1

}
