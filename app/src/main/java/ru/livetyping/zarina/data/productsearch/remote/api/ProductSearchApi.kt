package ru.livetyping.zarina.data.productsearch.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.data.productsearch.remote.api.dto.SearchSuggestionsDto
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class ProductSearchApi @Inject constructor(
    @Qualifiers.AnyQuery(Qualifiers.AnyQueryType.SEARCH)
    private val searchHttpClient: HttpClient,

    @Qualifiers.AnyQuery(Qualifiers.AnyQueryType.AUTOCOMPLETE)
    private val autocompleteHttpClient: HttpClient,
) {
    suspend fun getSearchSuggestions(query: String): SearchSuggestionsDto {
        return autocompleteHttpClient.get("autocomplete") {
            parameter("st", query)
            parameter("strategy", "vectors_extended,zero_queries")
            parameter("apiKey", BuildConfig.ANY_QUERY_KEY)

            timeout {
                // TODO: [High] Set timeout to 2 seconds
            }
        }.body()
    }
}
