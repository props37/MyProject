package ru.zarina.zarina.data.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.local.ISearchLocalSource

@Factory
class SearchRepository(
    private val local: ISearchLocalSource,
) : ISearchRepository {

    override suspend fun getHistory() = local.getHistory()

    override suspend fun addToHistory(query: String) = local.addToHistory(query)

    override suspend fun removeFromHistory(query: String) = local.removeFromHistory(query)

}
