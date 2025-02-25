package ru.livetyping.zarina.presentation.common.util.library.paging

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem

fun Flow<PagingData<ProductItem>>.mapProducts(
    favoriteProductIdsResultFlow: Flow<Result<Set<Product.Id>>>,
    cartProductIdsResultFlow: Flow<Result<Set<Product.Id>>>,
    filterDuplicates: Boolean = false,
): Flow<PagingData<ProductItem>> {
    return combine(
        this,
        favoriteProductIdsResultFlow,
        cartProductIdsResultFlow,
    ) { pagingData, favoriteProductIdsResult, cartProductIdsResult ->
        val favoriteProductIds = favoriteProductIdsResult.getOrDefault(emptySet())
        val cartProductIds = cartProductIdsResult.getOrDefault(emptySet())

        val transformedPagingData = if (filterDuplicates) {
            val keyHashSet = HashSet<String>()
            pagingData.filter { product ->
                keyHashSet.add(product.id.value)
            }
        } else {
            pagingData
        }

        transformedPagingData.mapProducts(favoriteProductIds, cartProductIds)
    }
}

fun PagingData<ProductItem>.mapProducts(
    favoriteProductIds: Set<Product.Id>,
    cartProductIds: Set<Product.Id>,
): PagingData<ProductItem> {
    return this.map { product ->
        product.copy(
            isInFavorites = product.id in favoriteProductIds,
            isInCart = product.id in cartProductIds,
        )
    }
}
