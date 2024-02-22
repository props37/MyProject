package ru.zarina.zarina.ui.common.paging

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.zarina.zarina.domain.rework.product.Product

fun Flow<PagingData<Product>>.mapProducts(
    favoriteProductIdsResultFlow: Flow<Result<Set<Product.Id>>>,
    cartProductIdsResultFlow: Flow<Result<Set<Product.Id>>>,
): Flow<PagingData<Product>> {
    return combine(
        this,
        favoriteProductIdsResultFlow,
        cartProductIdsResultFlow,
    ) { pagingData, favoriteProductIdsResult, cartProductIdsResult ->
        val favoriteProductIds = favoriteProductIdsResult.getOrDefault(emptySet())
        val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())
        pagingData.mapProducts(favoriteProductIds, cartProductIds)
    }
}

fun PagingData<Product>.mapProducts(
    favoriteProductIds: Set<Product.Id>,
    cartProductIds: Set<Product.Id>,
): PagingData<Product> {
    return this.map { product ->
        if (product.id in favoriteProductIds || product.id in cartProductIds) {
            product.copy(
                isInFavorites = product.id in favoriteProductIds,
                isInCart = product.id in cartProductIds,
            )
        } else {
            product
        }
    }
}
