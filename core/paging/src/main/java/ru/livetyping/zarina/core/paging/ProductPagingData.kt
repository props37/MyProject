package ru.livetyping.zarina.core.paging

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort

public fun Flow<PagingData<ProductShort>>.updateProducts(
    wishlistProductIdsFlow: Flow<Set<Product.Id>>,
    cartProductIdsFlow: Flow<Set<Product.Id>>,
): Flow<PagingData<ProductShort>> {
    return combine(
        this,
        wishlistProductIdsFlow,
        cartProductIdsFlow,
    ) { pagingData, favoriteProductIds, cartProductIds ->
        pagingData.updateProducts(favoriteProductIds, cartProductIds)
    }
}

private fun PagingData<ProductShort>.updateProducts(
    wishlistProductIds: Set<Product.Id>,
    cartProductIds: Set<Product.Id>,
): PagingData<ProductShort> {
    return this.map { product ->
        product.copy(
            isInFavorites = product.id in wishlistProductIds,
            isInCart = product.id in cartProductIds,
        )
    }
}
