package ru.livetyping.zarina.data.search.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.core.buildutil.AnyQueryApiKey
import ru.livetyping.zarina.core.network.di.AnyQueryAutocompleteApi
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchSuggestionsDto
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

internal class SearchApiImpl @Inject constructor(
    @AnyQueryAutocompleteApi
    private val autocompleteHttpClient: HttpClient,
    @AnyQueryApiKey
    private val apiKey: String,
) : SearchApi {
    override suspend fun getSearchSuggestions(query: String): SearchSuggestionsDto {
        return autocompleteHttpClient.get("autocomplete") {
            parameter("st", query)
            parameter("strategy", "vectors_extended,zero_queries")
            parameter("apiKey", apiKey)

            timeout {
                requestTimeoutMillis = 2.seconds.inWholeMilliseconds
            }
        }.body()
    }
}
