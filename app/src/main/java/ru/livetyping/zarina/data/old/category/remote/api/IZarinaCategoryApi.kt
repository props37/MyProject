package ru.livetyping.zarina.data.old.category.remote.api

import ru.livetyping.zarina.data.old.category.remote.api.dto.CategoryResponseDto

interface IZarinaCategoryApi {

    suspend fun getCategories(): CategoryResponseDto

}
