package ru.livetyping.zarina.data.old.search.remote.api

import ru.livetyping.zarina.data.old.search.remote.api.dto.SearchAutocompleteDto
import ru.livetyping.zarina.data.old.search.remote.api.dto.SearchResultDto
import ru.livetyping.zarina.data.old.search.remote.api.dto.SortDto

interface IAnyQuerySearchApi {
    suspend fun getAutocomplete(query: String): SearchAutocompleteDto
    suspend fun getSearchResults(
        query: String,
        offset: Int,
        sort: SortDto,
        filters: List<String>,
    ): SearchResultDto
}
