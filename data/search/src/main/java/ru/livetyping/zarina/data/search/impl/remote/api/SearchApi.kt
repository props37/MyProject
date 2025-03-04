package ru.livetyping.zarina.data.search.impl.remote.api

import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchResultDto
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchSuggestionsDto

internal interface SearchApi {
    suspend fun getSearchSuggestions(query: String): SearchSuggestionsDto

    suspend fun search(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        offset: Int,
    ): SearchResultDto
}
