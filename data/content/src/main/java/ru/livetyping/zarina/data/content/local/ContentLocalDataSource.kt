package ru.livetyping.zarina.data.content.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender

internal interface ContentLocalDataSource {
    fun getCatalogMenuFlow(): Flow<CatalogMenuByGender?>

    fun setCatalogMenu(menu: CatalogMenuByGender?)

    suspend fun clear()
}
