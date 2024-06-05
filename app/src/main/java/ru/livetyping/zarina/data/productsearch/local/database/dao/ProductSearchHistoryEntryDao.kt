package ru.livetyping.zarina.data.productsearch.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryEntryEntity

@Dao
abstract class ProductSearchHistoryEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveProductSearchHistoryEntry(entry: ProductSearchHistoryEntryEntity)

    fun getLastProductSearchHistoryEntriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryEntryEntity>> {
        return getLastProductSearchHistoryEntriesFlowImpl(text, limit).distinctUntilChanged()
    }

    @Query("DELETE FROM ${ProductSearchHistoryEntryEntity.TABLE_NAME}")
    abstract suspend fun clear()

    @Query(
        """
            SELECT *
            FROM ${ProductSearchHistoryEntryEntity.TABLE_NAME}
            WHERE ${ProductSearchHistoryEntryEntity.FIELD_TEXT} LIKE '%' || :text || '%'
            ORDER BY ${ProductSearchHistoryEntryEntity.FIELD_TIMESTAMP_MILLIS} DESC
            LIMIT :limit
        """
    )
    protected abstract fun getLastProductSearchHistoryEntriesFlowImpl(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryEntryEntity>>
}
