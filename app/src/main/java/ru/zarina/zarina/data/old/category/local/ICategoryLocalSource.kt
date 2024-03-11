package ru.zarina.zarina.data.old.category.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.Category

interface ICategoryLocalSource {
    suspend fun addCategories(categories: List<Category>)
    fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Category.Id): Flow<Category?>
}
