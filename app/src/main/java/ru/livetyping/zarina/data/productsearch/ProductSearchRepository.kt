package ru.livetyping.zarina.data.productsearch

import ru.livetyping.zarina.data.productsearch.remote.ProductSearchRemoteDataSource
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRepository @Inject constructor(
    private val remoteDataSource: ProductSearchRemoteDataSource,
) {
    suspend fun getSearchSuggestions(query: String): ProductSearchSuggestions {
        return remoteDataSource.getSearchSuggestions(query)
    }
}
