package ru.zarina.zarina.data.old.search

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.FilteredProducts
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.Page
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.domain.old.SearchAutocomplete

interface ISearchRepository {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts>

    fun getHistory(limit: Int): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
