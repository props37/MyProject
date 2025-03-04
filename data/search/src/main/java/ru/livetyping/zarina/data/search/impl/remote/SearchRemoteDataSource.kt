package ru.livetyping.zarina.data.search.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions

internal interface SearchRemoteDataSource {
    fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions>

    fun search(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        offset: Int,
    ): Flow<SearchResult>
}
