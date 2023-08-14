package ru.zarina.zarina.data.search.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.search.local.database.entity.SearchHistoryEntity

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SearchHistoryEntity)

    @Query("SELECT * FROM search_history_table")
    fun select(): Flow<List<SearchHistoryEntity>>

    @Delete
    fun delete(query: SearchHistoryEntity)

}
