package ru.zarina.zarina.data.old.category.remote.api

import ru.zarina.zarina.data.old.category.remote.api.dto.CategoryResponseDto

interface IZarinaCategoryApi {

    suspend fun getCategories(): CategoryResponseDto

}
