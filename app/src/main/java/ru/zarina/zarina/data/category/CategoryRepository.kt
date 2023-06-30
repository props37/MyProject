package ru.zarina.zarina.data.category

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.category.local.ICategoryLocalSource
import ru.zarina.zarina.data.category.remote.ICategoryRemoteSource
import ru.zarina.zarina.domain.Category

@Factory
class CategoryRepository(
    private val local: ICategoryLocalSource,
    private val remote: ICategoryRemoteSource,
) : ICategoryRepository {

    override suspend fun fetchCategories(): List<Category> {
        val categories = remote.getCategories()
        local.addCategories(categories)
        return categories
    }

    override suspend fun getCategories(): Flow<List<Category>> = local.getCategories()

    override fun getCategory(id: Category.Id) = local.getCategory(id)

}

