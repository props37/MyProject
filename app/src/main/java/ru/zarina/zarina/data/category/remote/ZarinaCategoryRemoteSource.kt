package ru.zarina.zarina.data.category.remote

import ru.zarina.zarina.data.category.remote.api.IZarinaCategoryApi
import ru.zarina.zarina.domain.Category
import javax.inject.Inject

class ZarinaCategoryRemoteSource @Inject constructor(
    private val api: IZarinaCategoryApi,
) : ICategoryRemoteSource {

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().categories
            ?.mapNotNull { it.toDomain() }
            .orEmpty()
    }

}
