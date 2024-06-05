package ru.livetyping.zarina.data.productsearch.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryQueryEntity

@Dao
abstract class ProductSearchHistoryQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveProductSearchHistoryQuery(query: ProductSearchHistoryQueryEntity)

    fun getLastProductSearchHistoryQueriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryQueryEntity>> {
        return getLastProductSearchHistoryQueriesFlowImpl(text, limit).distinctUntilChanged()
    }

    @Query(
        """
            DELETE
            FROM ${ProductSearchHistoryQueryEntity.TABLE_NAME}
            WHERE ${ProductSearchHistoryQueryEntity.FIELD_TEXT} = :text
        """
    )
    abstract suspend fun deleteProductSearchHistoryQuery(text: String)

    @Query("DELETE FROM ${ProductSearchHistoryQueryEntity.TABLE_NAME}")
    abstract suspend fun clear()

    @Query(
        """
            SELECT *
            FROM ${ProductSearchHistoryQueryEntity.TABLE_NAME}
            WHERE ${ProductSearchHistoryQueryEntity.FIELD_TEXT} LIKE '%' || :text || '%'
            ORDER BY ${ProductSearchHistoryQueryEntity.FIELD_TIMESTAMP_MILLIS} DESC
            LIMIT :limit
        """
    )
    protected abstract fun getLastProductSearchHistoryQueriesFlowImpl(
        text: String,
        limit: Int,
    ): Flow<List<ProductSearchHistoryQueryEntity>>
}
