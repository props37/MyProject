package ru.livetyping.zarina.data.search.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.database.search.SearchHistoryQueryDao
import ru.livetyping.zarina.core.database.search.SearchHistoryQueryEntity
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import javax.inject.Inject

internal class SearchLocalDataSourceImpl @Inject constructor(
    private val searchHistoryQueryDao: SearchHistoryQueryDao,
) : SearchLocalDataSource {
    override fun getLastSearchHistoryQueriesFlow(
        query: String,
        limit: Int
    ): Flow<List<SearchHistoryQuery>> {
        return searchHistoryQueryDao.getLastSearchHistoryQueriesFlow(query, limit).map { list ->
            list.map { it.toSearchHistoryQuery() }
        }
    }

    override suspend fun saveSearchHistoryQuery(query: SearchHistoryQuery) {
        val entity = SearchHistoryQueryEntity.from(query)
        searchHistoryQueryDao.saveSearchHistoryQuery(entity)
    }

    override suspend fun deleteSearchHistoryQuery(query: String) {
        searchHistoryQueryDao.deleteSearchHistoryQuery(query)
    }

    override suspend fun clearSearchHistory() {
        searchHistoryQueryDao.clear()
    }

    override suspend fun clear() {
        searchHistoryQueryDao.clear()
    }
}
