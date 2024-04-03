package ru.livetyping.zarina.data.old.category.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Category

interface ICategoryLocalSource {
    suspend fun addCategories(categories: List<Category>)
    fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Category.Id): Flow<Category?>
}
