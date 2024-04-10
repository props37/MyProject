package ru.livetyping.zarina.usecase.favorite

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.BasicUseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.data.favorite.pagination.FavoriteProductPagingSource
import ru.livetyping.zarina.domain.product.Product
import javax.inject.Inject

// TODO: [Medium] Move logic to repository

class GetFavoriteProductPagingDataFlowUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : BasicUseCase<Unit, Flow<PagingData<Product>>> {

    override fun invoke(params: Unit): Flow<PagingData<Product>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                FavoriteProductPagingSource(favoriteRepository)
            },
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

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 200
    }
}
