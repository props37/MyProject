package ru.livetyping.zarina.data.productsearch

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.productsearch.remote.ProductSearchRemoteDataSource
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRepository @Inject constructor(
    private val remoteDataSource: ProductSearchRemoteDataSource,
) {
    fun getSearchSuggestionsFlow(query: String): Flow<ProductSearchSuggestions> {
        return remoteDataSource.getSearchSuggestionsFlow(query)
    }
}
