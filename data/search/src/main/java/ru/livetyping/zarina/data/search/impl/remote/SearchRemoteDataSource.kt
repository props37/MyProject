package ru.livetyping.zarina.data.search.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions

internal interface SearchRemoteDataSource {
    fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions>
}
