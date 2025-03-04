package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions

public interface SearchRepository {
    public fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions>

    public fun search(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        offset: Int,
    ): Flow<SearchResult>

    public fun getLastSearchHistoryQueriesFlow(
        query: String,
        limit: Int,
    ): Flow<List<SearchHistoryQuery>>

    public suspend fun saveSearchHistoryQuery(query: SearchHistoryQuery)

    public suspend fun deleteSearchHistoryQuery(query: String)

    public suspend fun clearSearchHistory()

    public suspend fun clear()
}
