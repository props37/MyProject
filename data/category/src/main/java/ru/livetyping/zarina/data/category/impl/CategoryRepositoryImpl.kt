package ru.livetyping.zarina.data.category.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CacheExpirationPolicy
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.find
import ru.livetyping.zarina.core.domain.repository.CategoryRepository
import ru.livetyping.zarina.data.category.impl.local.CategoryLocalDataSource
import ru.livetyping.zarina.data.category.impl.remote.CategoryRemoteDataSource
import timber.log.Timber
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

    private fun getCategoriesFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<Categories> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("Categories CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
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
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("Category CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
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
