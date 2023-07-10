package ru.zarina.zarina.data.category.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.category.remote.api.IZarinaCategoryApi
import ru.zarina.zarina.domain.Category

@Factory
class ZarinaCategoryRemoteSource(
    private val api: IZarinaCategoryApi,
) : ICategoryRemoteSource {

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().categories
            ?.mapNotNull { it.toDomain() }
            .orEmpty()
    }

}
