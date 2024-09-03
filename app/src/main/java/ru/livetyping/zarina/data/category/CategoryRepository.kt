package ru.livetyping.zarina.data.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.category.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.remote.CategoryRemoteDataSource
import ru.livetyping.zarina.domain.category.Categories
import ru.livetyping.zarina.domain.category.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
) {
    fun getCategoriesFlow(): Flow<Categories> {
        return localDataSource.getCategoriesFlow()
            .onEach { cached ->
                if (cached == null) {
                    fetchCategories()
                }
            }
            .filterNotNull()
    }

    fun getCategoryFlow(id: Category.Id): Flow<Category> {
        return localDataSource.getCategoryFlow(id)
            .onEach { cached ->
                if (cached == null) {
                    fetchCategories()
                }
            }
            .filterNotNull()
    }

    private suspend fun fetchCategories(): Categories {
        val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
        checkNotNull(categories) { "Failed to fetch categories" }
        localDataSource.setCategories(categories)
        return categories
    }
}
