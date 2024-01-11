package ru.zarina.zarina.data.rework.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.category.local.CategoryLocalDataSource
import ru.zarina.zarina.data.rework.category.remote.CategoryRemoteDataSource
import ru.zarina.zarina.domain.rework.common.Category
import timber.log.Timber
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
) {
    fun getCategories(): Flow<List<Category>> = flow {
        val cached = localDataSource.getCategories().firstOrNull()
        if (cached != null) {
            Timber.v("Get cached categories")
            emit(cached)
        } else {
            val categories = remoteDataSource.getCategories().firstOrNull()
            checkNotNull(categories) { "Failed to fetch categories" }
            localDataSource.setCategories(categories)
            emit(categories)
        }
    }
}
