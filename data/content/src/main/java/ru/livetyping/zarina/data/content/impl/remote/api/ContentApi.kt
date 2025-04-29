package ru.livetyping.zarina.data.content.impl.remote.api

import ru.livetyping.zarina.data.content.impl.remote.api.dto.CatalogMenuByGenderDto

internal interface ContentApi {
    suspend fun getCatalogMenu(): CatalogMenuByGenderDto
}
