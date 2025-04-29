package ru.livetyping.zarina.data.content.impl

import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import ru.livetyping.zarina.data.content.impl.remote.ContentRemoteDataSource
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
    private val remoteDataSource: ContentRemoteDataSource,
) : ContentRepository {
    override suspend fun getCatalogMenu(): CatalogMenuByGender {
        return remoteDataSource.getCatalogMenu()
    }

    override suspend fun clear() {
        localDataSource.clear()
    }
}
