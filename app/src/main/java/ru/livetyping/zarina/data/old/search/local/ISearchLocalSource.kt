package ru.livetyping.zarina.data.old.search.local

import kotlinx.coroutines.flow.Flow

interface ISearchLocalSource {
    fun getHistory(limit: Int): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
