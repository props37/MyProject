package ru.livetyping.zarina.data.productsearch

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.productsearch.local.ProductSearchLocalDataSource
import ru.livetyping.zarina.data.productsearch.remote.ProductSearchRemoteDataSource
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRepository @Inject constructor(
    private val remoteDataSource: ProductSearchRemoteDataSource,
    private val localDataSource: ProductSearchLocalDataSource,
) {
    fun getSearchSuggestionsFlow(query: String): Flow<ProductSearchSuggestions> {
        return remoteDataSource.getSearchSuggestionsFlow(query)
    }

    fun searchProductsFlow(
        query: String,
        sorting: Sorting,
        filters: Filters?,
        offset: Int,
    ): Flow<ProductSearchResult> {
        return remoteDataSource.searchProductsFlow(query, sorting, filters, offset)
    }

    fun getLastProductSearchHistoryQueriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryQuery>> {
        return localDataSource.getLastProductSearchHistoryQueriesFlow(text, limit)
    }

    suspend fun saveProductSearchHistoryQuery(entry: ProductSearchHistoryQuery) {
        localDataSource.saveProductSearchHistoryQuery(entry)
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
