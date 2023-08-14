package ru.zarina.zarina.data.search.local

import kotlinx.coroutines.flow.Flow

interface ISearchLocalSource {
    suspend fun getHistory(): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
