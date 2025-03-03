package ru.livetyping.zarina.data.search.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery

internal interface SearchLocalDataSource {
    fun getLastSearchHistoryQueriesFlow(
        query: String,
        limit: Int,
    ): Flow<List<SearchHistoryQuery>>

    suspend fun saveSearchHistoryQuery(query: SearchHistoryQuery)

    suspend fun deleteSearchHistoryQuery(query: String)

    suspend fun clearSearchHistory()

    suspend fun clear()
}
