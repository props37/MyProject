package ru.livetyping.zarina.data.category.impl.remote.api

import ru.livetyping.zarina.data.category.impl.remote.api.dto.CategoriesDto

internal interface CategoryApi {
    suspend fun getCategories(): CategoriesDto
}
