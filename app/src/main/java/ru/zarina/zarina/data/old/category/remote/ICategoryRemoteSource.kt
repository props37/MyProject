package ru.zarina.zarina.data.old.category.remote

import ru.zarina.zarina.domain.Category

interface ICategoryRemoteSource {
    suspend fun getCategories(): List<Category>
}
