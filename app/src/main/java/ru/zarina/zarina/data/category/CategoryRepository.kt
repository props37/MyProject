package ru.zarina.zarina.data.category

import ru.zarina.zarina.data.category.remote.ICategoryRemoteSource
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val remote: ICategoryRemoteSource,
) : ICategoryRepository {
    override suspend fun getCategories() = remote.getCategories()
}

