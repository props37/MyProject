package ru.livetyping.zarina.data.productsearch

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.productsearch.remote.ProductSearchRemoteDataSource
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRepository @Inject constructor(
    private val remoteDataSource: ProductSearchRemoteDataSource,
) {
    fun getSearchSuggestionsFlow(query: String): Flow<ProductSearchSuggestions> {
        return remoteDataSource.getSearchSuggestionsFlow(query)
    }

    fun searchProductsFlow(query: String, sorting: Sorting, offset: Int): Flow<ProductSearchResult> {
        return remoteDataSource.searchProductsFlow(query, sorting, offset)
    }
}
