package ru.zarina.zarina.data.category.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.zarina.zarina.data.category.local.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategories(categories: List<CategoryEntity>)

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

}
