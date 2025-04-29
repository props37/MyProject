package ru.livetyping.zarina.data.content.impl.local.catalog

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender

internal interface CatalogDataHolder {
    fun getCatalogMenuFlow(): Flow<CatalogMenuByGender?>

    fun setCatalogMenu(menu: CatalogMenuByGender?)

    fun clear()
}
