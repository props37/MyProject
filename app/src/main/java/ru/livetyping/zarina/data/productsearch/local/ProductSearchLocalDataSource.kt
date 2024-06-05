package ru.livetyping.zarina.data.productsearch.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryEntryDao
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryEntryEntity
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryEntry
import javax.inject.Inject

class ProductSearchLocalDataSource @Inject constructor(
    private val productSearchHistoryEntryDao: ProductSearchHistoryEntryDao,
) {
    fun getLastProductSearchHistoryEntriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryEntry>> {
        return productSearchHistoryEntryDao.getLastProductSearchHistoryEntriesFlow(text, limit)
            .map { list ->
                list.map { it.toProductSearchHistoryEntry() }
            }
    }

    suspend fun saveProductSearchHistoryEntry(entry: ProductSearchHistoryEntry) {
        val entity = ProductSearchHistoryEntryEntity.from(entry)
        productSearchHistoryEntryDao.saveProductSearchHistoryEntry(entity)
    }

    suspend fun clear() {
        productSearchHistoryEntryDao.clear()
    }
}
