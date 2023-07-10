package ru.zarina.zarina.data.category.remote.api

import ru.zarina.zarina.data.category.remote.api.dto.CategoryResponseDto

interface IZarinaCategoryApi {

    suspend fun getCategories(): CategoryResponseDto

}
