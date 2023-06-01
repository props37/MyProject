package ru.zarina.zarina.data.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.zarina.zarina.data.category.local.database.CategoryDao
import ru.zarina.zarina.data.category.local.database.entity.CategoryEntity

@Database(
    version = 1, exportSchema = true, entities = [
        CategoryEntity::class
    ]
)
@TypeConverters(TypeConverter::class)
abstract class ZarinaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

}
