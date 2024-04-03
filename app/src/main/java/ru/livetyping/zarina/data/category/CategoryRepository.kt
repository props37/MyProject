package ru.livetyping.zarina.data.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.category.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.remote.CategoryRemoteDataSource
import ru.livetyping.zarina.domain.category.Categories
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.category.find
import timber.log.Timber
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
) {
    fun getCategoriesFlow(): Flow<Categories> = flow {
        val cached = localDataSource.getCategoriesFlow().firstOrNull()
        if (cached != null) {
            Timber.v("Get cached categories")
            emit(cached)
        } else {
            val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
            checkNotNull(categories) { "Failed to fetch categories" }
            localDataSource.setCategories(categories)
            emit(categories)
        }
    }

    fun getCategoryFlow(id: Category.Id): Flow<Category> = flow {
        val cached = localDataSource.getCategoryFlow(id).firstOrNull()
        if (cached != null) {
            Timber.v("Get cached category")
            emit(cached)
        } else {
            val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
            checkNotNull(categories) { "Failed to fetch categories" }
            localDataSource.setCategories(categories)

            val category = categories.find { it.id == id }
            checkNotNull(category) { "Failed to find Category ${id.value}" }
            emit(category)
        }
    }
}
