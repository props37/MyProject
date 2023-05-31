package ru.zarina.zarina.data.category

import ru.zarina.zarina.domain.Category

interface ICategoryRepository {
    suspend fun getCategories(): List<Category>
}
