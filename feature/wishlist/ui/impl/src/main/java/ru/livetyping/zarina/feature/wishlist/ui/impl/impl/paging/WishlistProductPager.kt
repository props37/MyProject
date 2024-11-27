package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import javax.inject.Inject
import javax.inject.Provider

internal class WishlistProductPager @Inject constructor(
    private val wishlistProductPagingSource: Provider<WishlistProductPagingSource>,
) {
    fun getWishlistProductPagingDataFlow(): Flow<PagingData<ProductShort>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = { wishlistProductPagingSource.get() },
        ).flow
    }

    private fun getPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE,
        )
    }

    private companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 200
    }
}
