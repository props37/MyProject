package ru.zarina.zarina.ui.screens.catalog.products.paging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Page

class ProductPageHolder {

    private val _pages = MutableStateFlow(mapOf<Int, Page<FilteredProducts>>())
    val pages = _pages.asStateFlow()

    fun insert(page: Page<FilteredProducts>) {
        _pages.update { it + (page.pagination.currentPageIndex to page) }
    }

    fun getPage(pageIndex: Int): Page<FilteredProducts>? {
        return _pages.value[pageIndex]
    }

}