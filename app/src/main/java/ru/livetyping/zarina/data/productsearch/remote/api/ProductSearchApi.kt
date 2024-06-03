package ru.livetyping.zarina.data.productsearch.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.data.productsearch.remote.api.dto.ProductSearchSortingDto
import ru.livetyping.zarina.data.productsearch.remote.api.dto.SearchProductsDto
import ru.livetyping.zarina.data.productsearch.remote.api.dto.SearchProductsRequestBody
import ru.livetyping.zarina.data.productsearch.remote.api.dto.SearchSuggestionsDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ProductSearchApi @Inject constructor(
    @Qualifiers.AnyQuery(Qualifiers.AnyQueryType.AUTOCOMPLETE)
    private val autocompleteHttpClient: HttpClient,

    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getSearchSuggestions(query: String): SearchSuggestionsDto {
        return autocompleteHttpClient.get("autocomplete") {
            parameter("st", query)
            parameter("strategy", "vectors_extended,zero_queries")
            parameter("apiKey", BuildConfig.ANY_QUERY_KEY)

            timeout {
                requestTimeoutMillis = 2.seconds.inWholeMilliseconds
            }
        }.body()
    }

    suspend fun searchProducts(
        query: String,
        sorting: Sorting,
        offset: Int,
    ): SearchProductsDto {
        val body = SearchProductsRequestBody(
            query = query,
            sort = ProductSearchSortingDto.from(sorting),
            offset = offset,
        )
        return httpClient.post("/api/search/") {
            setJsonBody(body)
        }.body()
    }
}
