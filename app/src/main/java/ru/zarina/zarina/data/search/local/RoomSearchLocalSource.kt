package ru.zarina.zarina.data.search.local

import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.search.local.database.SearchDao
import ru.zarina.zarina.data.search.local.database.entity.SearchHistoryEntity

@Factory
class RoomSearchLocalSource(
    private val dao: SearchDao,
) : ISearchLocalSource {
    override fun getHistory(limit: Int) = dao.selectLatest(limit)
        .map { list -> list.map { it.toDomain() } }

    override suspend fun addToHistory(query: String) {
        dao.insert(SearchHistoryEntity.from(query))
    }

    override suspend fun removeFromHistory(query: String) {
        dao.delete(SearchHistoryEntity.from(query))
    }
}
