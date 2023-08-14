package ru.zarina.zarina.data.search

import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    suspend fun getHistory(): Flow<List<String>>
    suspend fun addToHistory(query: String)
    suspend fun removeFromHistory(query: String)
}
