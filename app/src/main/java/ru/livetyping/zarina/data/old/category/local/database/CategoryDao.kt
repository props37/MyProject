package ru.livetyping.zarina.data.old.category.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.old.category.local.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories")
    fun select(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun select(id: Int): Flow<CategoryEntity?>

}
