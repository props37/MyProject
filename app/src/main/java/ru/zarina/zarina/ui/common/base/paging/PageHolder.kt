package ru.zarina.zarina.ui.common.base.paging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.Page

class PageHolder<T> {
    private val _pages = MutableStateFlow(mapOf<Int, Page<T>>())
    val pages = _pages.asStateFlow()

    fun insert(page: Page<T>) {
        _pages.update { it + (page.pagination.currentPageIndex to page) }
    }

    fun getPage(pageIndex: Int): Page<T>? {
        return _pages.value[pageIndex]
    }
}
