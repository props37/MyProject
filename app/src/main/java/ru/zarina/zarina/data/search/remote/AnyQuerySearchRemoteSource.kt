package ru.zarina.zarina.data.search.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.remote.api.IAnyQuerySearchApi

@Factory
class AnyQuerySearchRemoteSource(
    private val api: IAnyQuerySearchApi,
) : ISearchRemoteSource {

    override suspend fun getAutocomplete(query: String) = api.getAutocomplete(query).toDomain()

}
