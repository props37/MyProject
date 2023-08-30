package ru.zarina.zarina.data.search.remote.api

import ru.zarina.zarina.data.search.remote.api.dto.SearchAutocompleteDto
import ru.zarina.zarina.data.search.remote.api.dto.SearchResultDto
import ru.zarina.zarina.data.search.remote.api.dto.SortDto

interface IAnyQuerySearchApi {
    suspend fun getAutocomplete(query: String): SearchAutocompleteDto
    suspend fun getSearchResults(
        query: String,
        offset: Int,
        sort: SortDto,
        filters: List<String>,
    ): SearchResultDto
}
