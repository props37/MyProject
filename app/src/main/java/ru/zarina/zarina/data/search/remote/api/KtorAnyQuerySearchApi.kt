package ru.zarina.zarina.data.search.remote.api

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.remote.api.dto.SearchAutocompleteDto

@Factory
class KtorAnyQuerySearchApi : IAnyQuerySearchApi {
    override fun getAutocomplete(query: String): SearchAutocompleteDto {
        TODO("Not yet implemented")
    }
}
