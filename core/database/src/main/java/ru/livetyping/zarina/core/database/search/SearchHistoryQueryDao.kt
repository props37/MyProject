package ru.livetyping.zarina.core.database.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
public abstract class SearchHistoryQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract suspend fun saveSearchHistoryQuery(query: SearchHistoryQueryEntity)

    public fun getLastSearchHistoryQueriesFlow(
        text: String,
        limit: Int,
    ): Flow<List<SearchHistoryQueryEntity>> {
        return getLastSearchHistoryQueriesFlowImpl(text, limit).distinctUntilChanged()
    }

    @Query(
        """
            DELETE
            FROM ${SearchHistoryQueryEntity.TABLE_NAME}
            WHERE ${SearchHistoryQueryEntity.FIELD_TEXT} = :text
        """
    )
    public abstract suspend fun deleteSearchHistoryQuery(text: String)

    @Query("DELETE FROM ${SearchHistoryQueryEntity.TABLE_NAME}")
    public abstract suspend fun clear()

    @Query(
        """
            SELECT *
            FROM ${SearchHistoryQueryEntity.TABLE_NAME}
            WHERE ${SearchHistoryQueryEntity.FIELD_TEXT} LIKE '%' || :text || '%'
            ORDER BY ${SearchHistoryQueryEntity.FIELD_TIMESTAMP_MILLIS} DESC
            LIMIT :limit
        """
    )
    protected abstract fun getLastSearchHistoryQueriesFlowImpl(
        text: String,
        limit: Int,
    ): Flow<List<SearchHistoryQueryEntity>>
}
