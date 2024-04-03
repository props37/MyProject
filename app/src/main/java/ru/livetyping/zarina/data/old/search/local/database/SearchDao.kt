package ru.livetyping.zarina.data.old.search.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.old.search.local.database.entity.SearchHistoryEntity

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SearchHistoryEntity)

    @Query("SELECT * FROM search_history_table ORDER BY id DESC LIMIT :limit")
    fun selectLatest(limit: Int): Flow<List<SearchHistoryEntity>>

    @Delete
    fun delete(query: SearchHistoryEntity)

}
