package ru.livetyping.zarina.data.content.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.data.content.impl.local.catalog.CatalogDataHolder
import javax.inject.Inject

internal class ContentLocalDataSourceImpl @Inject constructor(
    private val catalogDataHolder: CatalogDataHolder,
) : ContentLocalDataSource {
    override fun getCatalogMenuFlow(): Flow<CatalogMenuByGender?> {
        return catalogDataHolder.getCatalogMenuFlow()
    }

    override fun setCatalogMenu(menu: CatalogMenuByGender?) {
        catalogDataHolder.setCatalogMenu(menu)
    }

    override suspend fun clear() {
        catalogDataHolder.clear()
    }
}
