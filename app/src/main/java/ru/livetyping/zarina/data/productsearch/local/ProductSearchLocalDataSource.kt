package ru.livetyping.zarina.data.productsearch.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryQueryDao
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryQueryEntity
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery
import javax.inject.Inject

class ProductSearchLocalDataSource @Inject constructor(
    private val productSearchHistoryQueryDao: ProductSearchHistoryQueryDao,
) {
    fun getLastProductSearchHistoryQueriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryQuery>> {
        return productSearchHistoryQueryDao.getLastProductSearchHistoryQueriesFlow(
            text = text.lowercase(),
            limit = limit,
        ).map { list ->
            list.map { it.toProductSearchHistoryQuery() }
        }
    }

    suspend fun saveProductSearchHistoryQuery(query: ProductSearchHistoryQuery) {
        val entity = ProductSearchHistoryQueryEntity.from(query)
        productSearchHistoryQueryDao.saveProductSearchHistoryQuery(entity)
    }

    suspend fun deleteProductSearchHistoryQuery(text: String) {
        productSearchHistoryQueryDao.deleteProductSearchHistoryQuery(text.lowercase())
    }

    suspend fun clear() {
        productSearchHistoryQueryDao.clear()
    }
}
