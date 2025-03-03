package ru.livetyping.zarina.data.search.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.data.search.impl.local.SearchLocalDataSource
import ru.livetyping.zarina.data.search.impl.remote.SearchRemoteDataSource
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: SearchLocalDataSource,
) : SearchRepository {
    override fun getSearchSuggestionsFlow(query: String): Flow<SearchSuggestions> {
        return remoteDataSource.getSearchSuggestionsFlow(query)
    }

    override fun getLastSearchHistoryQueriesFlow(
        query: String,
        limit: Int
    ): Flow<List<SearchHistoryQuery>> {
        return localDataSource.getLastSearchHistoryQueriesFlow(query, limit)
    }

    override suspend fun saveSearchHistoryQuery(query: SearchHistoryQuery) {
        localDataSource.saveSearchHistoryQuery(query)
    }

    override suspend fun deleteSearchHistoryQuery(query: String) {
        localDataSource.deleteSearchHistoryQuery(query)
    }

    override suspend fun clearSearchHistory() {
        localDataSource.clearSearchHistory()
    }

    override suspend fun clear() {
        localDataSource.clear()
    }
}
