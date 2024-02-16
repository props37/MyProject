package ru.zarina.zarina.ui.common.paging

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.zarina.zarina.domain.rework.product.Product

fun Flow<PagingData<Product>>.mapFavorites(
    favoriteProductIdsResultFlow: Flow<Result<Set<Product.Id>>>,
): Flow<PagingData<Product>> {
    return this.combine(favoriteProductIdsResultFlow) { pagingData, favoriteProductIdsResult ->
        val favoriteProductIds = favoriteProductIdsResult.getOrNull() ?: emptySet()
        pagingData.mapFavorites(favoriteProductIds)
    }
}

fun PagingData<Product>.mapFavorites(favoriteProductIds: Set<Product.Id>): PagingData<Product> {
    return this.map { product ->
        if (product.id in favoriteProductIds) product.copy(isInFavorites = true) else product
    }
}
