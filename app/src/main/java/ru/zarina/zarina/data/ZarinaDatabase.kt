package ru.zarina.zarina.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.zarina.zarina.data.category.local.database.CategoryDao
import ru.zarina.zarina.data.category.local.database.entity.CategoryEntity

@Database(
    version = 1, exportSchema = true, entities = [
        CategoryEntity::class
    ]
)
abstract class ZarinaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

}
