package ru.livetyping.zarina.data.category.remote.api

import ru.livetyping.zarina.data.category.remote.api.dto.CategoriesDto

internal interface CategoryApi {
    suspend fun getCategories(): CategoriesDto
}
