package ru.zarina.zarina.data.search.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.search.remote.api.dto.SearchAutocompleteDto
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorAnyQuerySearchApi(
    @Named(Qualifiers.Api.ANYQUERY_AUTOCOMPLETE)
    private val client: HttpClient,
) : IAnyQuerySearchApi {

    override suspend fun getAutocomplete(query: String): SearchAutocompleteDto {
        val response = client.get("autocomplete") {
            parameter("st", query.filter { it.isLetter() || it.isWhitespace() })
            parameter("shuffle", false)
            parameter("strategy", "vectors_extended,zero_queries")
            parameter("productsSize", 36)
            parameter("fullData", false)
            parameter("regionId", "global")

            parameter("apiKey", BuildConfig.ANYQUERY_SECRET)
        }
        return response.body()
    }

}
