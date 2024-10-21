package ru.livetyping.zarina.data.category.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.data.category.impl.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.impl.remote.CategoryRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val localDataSource: CategoryLocalDataSource,
    private val remoteDataSource: CategoryRemoteDataSource,
) : CategoryRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCategoriesFlow(): Flow<Categories> {
        return localDataSource.getCategoriesFlow()
            .mapLatest { cached ->
                Timber.tag(TAG).v("Cached categories: $cached")
                cached ?: fetchCategories()
            }
    }

    private suspend fun fetchCategories(): Categories {
        val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
        checkNotNull(categories) { "Failed to fetch categories" }
        localDataSource.setCategories(categories)
        return categories
    }

    private companion object {
        private const val TAG = "CategoryRepositoryImpl"
    }
}
