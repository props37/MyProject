package ru.zarina.zarina.data.old.search.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.old.search.remote.api.dto.SearchAutocompleteDto
import ru.zarina.zarina.data.old.search.remote.api.dto.SearchResultDto
import ru.zarina.zarina.data.old.search.remote.api.dto.SortDto
import ru.zarina.zarina.di.old.Qualifiers

@Factory
class KtorAnyQuerySearchApi(
    @Named(Qualifiers.Api.ANYQUERY_AUTOCOMPLETE)
    private val autocompleteClient: HttpClient,
    @Named(Qualifiers.Api.ANYQUERY_SEARCH)
    private val searchClient: HttpClient,
) : IAnyQuerySearchApi {

    override suspend fun getAutocomplete(query: String): SearchAutocompleteDto {
        val response = autocompleteClient.get("autocomplete") {
            parameter("st", query.filter { it.isLetter() || it.isWhitespace() })
            parameter("shuffle", false)
            parameter("strategy", "vectors_extended,zero_queries")
            parameter("productsSize", 36)
            parameter("fullData", false)
            parameter("regionId", "global")

            parameter("apiKey", BuildConfig.ANY_QUERY_KEY)
        }
        return response.body()
    }

    override suspend fun getSearchResults(
        query: String,
        offset: Int,
        sort: SortDto,
        filters: List<String>,
    ): SearchResultDto {
        val response = searchClient.get("search") {
            parameter("st", query.filter { it.isLetter() || it.isWhitespace() })
            parameter("strategy", "advanced,zero_queries_predictor")
            parameter("size", SEARCH_PAGE_SIZE)
            parameter("offset", offset)
            parameter("fullData", true)
            parameter("useCategoryPrediction", false)
            parameter("withCorrection", true)
            parameter("withFacets", true)
            parameter("treeFacets", true)
            parameter("showUnavailable", true)
            parameter("sort", sort)
            parameter("regionId", "global")

            filters.forEach {
                parameter("filter", it)
            }

            parameter("apiKey", BuildConfig.ANY_QUERY_KEY)
        }

        return response.body()
    }

    companion object {
        private const val SEARCH_PAGE_SIZE = 10
    }
}
