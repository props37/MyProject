package ru.livetyping.zarina.data.old.category.remote

import ru.livetyping.zarina.domain.old.Category

interface ICategoryRemoteSource {
    suspend fun getCategories(): List<Category>
}
