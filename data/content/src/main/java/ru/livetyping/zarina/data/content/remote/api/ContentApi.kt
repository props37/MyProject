package ru.livetyping.zarina.data.content.remote.api

import ru.livetyping.zarina.data.content.remote.api.dto.CatalogMenuByGenderDto

internal interface ContentApi {
    suspend fun getCatalogMenu(): CatalogMenuByGenderDto
}
