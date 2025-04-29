package ru.livetyping.zarina.core.domain.repository

import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender

public interface ContentRepository {
    public suspend fun getCatalogMenu(): CatalogMenuByGender

    public suspend fun clear()
}
