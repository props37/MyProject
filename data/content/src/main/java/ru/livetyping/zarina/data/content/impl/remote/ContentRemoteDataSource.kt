package ru.livetyping.zarina.data.content.impl.remote

import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender

internal interface ContentRemoteDataSource {
    suspend fun getCatalogMenu(): CatalogMenuByGender
}
