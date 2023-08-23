package ru.zarina.zarina.data.search

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.domain.SearchAutocomplete

interface ISearchRepository {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        pageIndex: Int,
    ): Page<FilteredProducts>

    fun getHistory(limit: Int): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
