package ru.livetyping.zarina.data.content.local.catalog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import javax.inject.Inject

internal class CatalogDataHolderImpl @Inject constructor() : CatalogDataHolder {
    private val catalogMenu = MutableStateFlow<CatalogMenuByGender?>(null)

    override fun getCatalogMenuFlow(): Flow<CatalogMenuByGender?> {
        return catalogMenu.asStateFlow()
    }

    override fun setCatalogMenu(menu: CatalogMenuByGender?) {
        catalogMenu.value = menu
    }

    override fun clear() {
        catalogMenu.value = null
    }
}
