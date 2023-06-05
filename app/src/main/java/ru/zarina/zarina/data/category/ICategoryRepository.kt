package ru.zarina.zarina.data.category

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Category

interface ICategoryRepository {
    suspend fun fetchCategories(): List<Category>
    suspend fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Category.Id): Flow<Category?>
}
