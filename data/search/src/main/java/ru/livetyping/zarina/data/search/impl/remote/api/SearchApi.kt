package ru.livetyping.zarina.data.search.impl.remote.api

import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchSuggestionsDto

internal interface SearchApi {
    suspend fun getSearchSuggestions(query: String): SearchSuggestionsDto
}
