package ru.zarina.zarina.data.search.remote

import ru.zarina.zarina.domain.SearchAutocomplete

interface ISearchRemoteSource {
    fun getAutocomplete(query: String): SearchAutocomplete
}
