package ru.zarina.zarina.data.search.remote.api

import ru.zarina.zarina.data.search.remote.api.dto.SearchAutocompleteDto

interface IAnyQuerySearchApi {
    fun getAutocomplete(query: String): SearchAutocompleteDto
}
