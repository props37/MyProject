package ru.zarina.zarina.data.category.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Category

interface ICategoryLocalSource {
    suspend fun addCategories(categories: List<Category>)
    fun getCategories(): Flow<List<Category>>
}
