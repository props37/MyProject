package ru.livetyping.zarina.data.search.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.core.buildutil.AnyQueryApiKey
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.network.di.AnyQueryAutocompleteApi
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchRequestBody
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchResultDto
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchSortingDto
import ru.livetyping.zarina.data.search.impl.remote.api.dto.SearchSuggestionsDto
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

internal class SearchApiImpl @Inject constructor(
    @AnyQueryAutocompleteApi
    private val autocompleteHttpClient: HttpClient,
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val zarinaHttpClient: HttpClient,
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

    override suspend fun search(
        query: String,
        sorting: ProductSorting,
        filters: ProductFilters?,
        offset: Int
    ): SearchResultDto {
        val body = SearchRequestBody(
            query = query,
            sorting = SearchSortingDto.from(sorting),
            filters = filters?.let { SearchRequestBody.Filters.from(it) },
            offset = offset,
        )
        return zarinaHttpClient.post("/api/search/") {
            setJsonBody(body)
        }.body()
    }
}
