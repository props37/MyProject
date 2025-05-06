package ru.livetyping.zarina.data.content.impl

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import ru.livetyping.zarina.data.content.impl.remote.ContentRemoteDataSource
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
    private val remoteDataSource: ContentRemoteDataSource,
) : ContentRepository {
    override suspend fun getCatalogMenu(cachePolicy: CachePolicy): CatalogMenuByGender {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> {
                val cached = localDataSource.getCatalogMenuFlow().firstOrNull()
                checkNotNull(cached) { "CatalogMenuByGender is not cached" }
                cached
            }

            is CachePolicy.LocalFirstThenRemote -> getCatalogMenuLocalFirstThenRemote(cachePolicy)
            is CachePolicy.Remote -> getCatalogMenuRemote(cachePolicy)
        }
    }

    override suspend fun clear() {
        localDataSource.clear()
    }

    private suspend fun getCatalogMenuLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): CatalogMenuByGender {
        val cached = localDataSource.getCatalogMenuFlow().firstOrNull()
        return if (cached != null) {
            cached
        } else {
            val menu = remoteDataSource.getCatalogMenu()
            catalogMenuCacheUpdatePolicyImpl(menu, cachePolicy.updatePolicy)
            return menu
        }
    }

    private suspend fun getCatalogMenuRemote(cachePolicy: CachePolicy.Remote): CatalogMenuByGender {
        val menu = remoteDataSource.getCatalogMenu()
        catalogMenuCacheUpdatePolicyImpl(menu, cachePolicy.updatePolicy)
        return menu
    }

    private fun catalogMenuCacheUpdatePolicyImpl(
        menu: CatalogMenuByGender,
        policy: CacheUpdatePolicy,
    ) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> localDataSource.setCatalogMenu(null)
            CacheUpdatePolicy.UPDATE -> localDataSource.setCatalogMenu(menu)
        }
    }

    private companion object {
        private const val TAG = "ContentRepositoryImpl"
    }
}
