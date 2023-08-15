package ru.zarina.zarina.data.search

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.SearchAutocomplete

interface ISearchRepository {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(query: String, pageIndex: Int): Page<List<Product>>
    fun getHistory(): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
