package ru.zarina.zarina.data.search.remote

import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.SearchAutocomplete

interface ISearchRemoteSource {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(query: String, pageIndex: Int): Page<List<Product>>
}
