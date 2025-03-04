package ru.livetyping.zarina.data.search.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.data.search.impl.remote.api.SearchApi
import javax.inject.Inject

internal class SearchRemoteDataSourceImpl @Inject constructor(
    private val api: SearchApi,
) : SearchRemoteDataSource {
    override fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions> = flow {
        val suggestions = api.getSearchSuggestions(query).toSearchSuggestions()
        emit(suggestions)
    }

    override fun search(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        offset: Int
    ): Flow<SearchResult> = flow {
        val result = api.search(query, sorting, filters, offset).toSearchResult()
        emit(result)
    }
}
