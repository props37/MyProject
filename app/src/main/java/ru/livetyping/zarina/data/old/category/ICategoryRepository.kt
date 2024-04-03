package ru.livetyping.zarina.data.old.category

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Category

interface ICategoryRepository {
    suspend fun fetchCategories(): List<Category>
    suspend fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Category.Id): Flow<Category?>
}
