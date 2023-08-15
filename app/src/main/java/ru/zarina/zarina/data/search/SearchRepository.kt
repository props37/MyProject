package ru.zarina.zarina.data.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.local.ISearchLocalSource
import ru.zarina.zarina.data.search.remote.ISearchRemoteSource

@Factory
class SearchRepository(
    private val local: ISearchLocalSource,
    private val remote: ISearchRemoteSource,
) : ISearchRepository {

    override suspend fun getAutocomplete(query: String) = remote.getAutocomplete(query)

    override suspend fun getSearchPage(query: String, pageIndex: Int) =
        remote.getSearchPage(query, pageIndex)

    override suspend fun getHistory() = local.getHistory()

    override suspend fun addToHistory(query: String) = local.addToHistory(query)

    override suspend fun removeFromHistory(query: String) = local.removeFromHistory(query)

}
