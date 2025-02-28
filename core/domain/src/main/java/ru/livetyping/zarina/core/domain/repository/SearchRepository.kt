package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions

public interface SearchRepository {
    public fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions>
}
