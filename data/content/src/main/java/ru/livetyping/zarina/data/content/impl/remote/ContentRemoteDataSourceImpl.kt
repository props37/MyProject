package ru.livetyping.zarina.data.content.impl.remote

import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.data.content.impl.remote.api.ContentApi
import javax.inject.Inject

internal class ContentRemoteDataSourceImpl @Inject constructor(
    private val api: ContentApi,
) : ContentRemoteDataSource {
    override suspend fun getCatalogMenu(): CatalogMenuByGender {
        return api.getCatalogMenu().toCatalogMenu()
    }
}
