package ru.zarina.zarina.data.search.remote

import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.domain.SearchAutocomplete

interface ISearchRemoteSource {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        pageIndex: Int,
    ): Page<FilteredProducts>
}
