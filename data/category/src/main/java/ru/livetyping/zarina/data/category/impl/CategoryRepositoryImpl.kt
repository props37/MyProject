package ru.livetyping.zarina.data.category.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryPath
import ru.livetyping.zarina.core.domain.model.category.find
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.data.category.impl.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.impl.remote.CategoryRemoteDataSource
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val localDataSource: CategoryLocalDataSource,
    private val remoteDataSource: CategoryRemoteDataSource,
) : CategoryRepository {
    override fun getCategoriesFlow(cachePolicy: CachePolicy): Flow<Categories> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getCategoriesFlow().filterNotNull()
            is CachePolicy.LocalFirstThenRemote -> {
                getCategoriesFlowLocalFirstThenRemote(cachePolicy)
            }

            is CachePolicy.Remote -> getCategoriesFlowRemote(cachePolicy)
        }
    }

    override fun getCategoryFlow(id: Category.Id, cachePolicy: CachePolicy): Flow<Category?> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getCategoryFlow(id)
            is CachePolicy.LocalFirstThenRemote -> {
                getCategoryFlowLocalFirstThenRemote(id, cachePolicy)
            }

            is CachePolicy.Remote -> getCategoryFlowRemote(id, cachePolicy)
        }
    }

    override suspend fun getCategoryPath(id: Category.Id, cachePolicy: CachePolicy): CategoryPath? {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getCategoryPath(id)
            is CachePolicy.LocalFirstThenRemote -> {
                getCategoryPathLocalFirstThenRemote(id, cachePolicy)
            }

            is CachePolicy.Remote -> getCategoryPathRemote(id, cachePolicy)
        }
    }

    private fun getCategoriesFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<Categories> {
        return localDataSource.getCategoriesFlow().map { cached ->
            if (cached != null) {
                cached
            } else {
                val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
                checkNotNull(categories) { "Failed to fetch categories" }
                categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
                categories
            }
        }
    }

    private fun getCategoriesFlowRemote(cachePolicy: CachePolicy.Remote): Flow<Categories> {
        return remoteDataSource.getCategoriesFlow()
            .onEach { categories ->
                categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
            }
    }

    private fun getCategoryFlowLocalFirstThenRemote(
        id: Category.Id,
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<Category?> {
        return localDataSource.getCategoryFlow(id).map { cached ->
            if (cached != null) {
                cached
            } else {
                val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
                checkNotNull(categories) { "Failed to fetch categories" }
                categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
                categories.find { it.id == id }
            }
        }
    }

    private fun getCategoryFlowRemote(
        id: Category.Id,
        cachePolicy: CachePolicy.Remote,
    ): Flow<Category?> {
        return remoteDataSource.getCategoriesFlow()
            .onEach { categories ->
                categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
            }
            .map { categories ->
                categories.find { it.id == id }
            }
    }

    private suspend fun getCategoryPathLocalFirstThenRemote(
        id: Category.Id,
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): CategoryPath? {
        val cached = localDataSource.getCategoryPath(id)
        return if (cached != null) {
            cached
        } else {
            val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
            checkNotNull(categories) { "Failed to fetch categories" }
            categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
            localDataSource.getCategoryPath(id)
        }
    }

    private suspend fun getCategoryPathRemote(
        id: Category.Id,
        cachePolicy: CachePolicy.Remote,
    ): CategoryPath? {
        val categories = remoteDataSource.getCategoriesFlow().firstOrNull()
        checkNotNull(categories) { "Failed to fetch categories" }
        categoriesCacheUpdatePolicyImpl(categories, cachePolicy.updatePolicy)
        return localDataSource.getCategoryPath(id)
    }

    private fun categoriesCacheUpdatePolicyImpl(categories: Categories, policy: CacheUpdatePolicy) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> localDataSource.setCategories(null)
            CacheUpdatePolicy.UPDATE -> localDataSource.setCategories(categories)
        }
    }

    private companion object {
        private const val TAG = "CategoryRepositoryImpl"
    }
}
