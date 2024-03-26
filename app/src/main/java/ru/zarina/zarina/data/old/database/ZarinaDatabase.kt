package ru.zarina.zarina.data.old.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.zarina.zarina.data.old.category.local.database.CategoryDao
import ru.zarina.zarina.data.old.category.local.database.entity.CategoryEntity
import ru.zarina.zarina.data.old.search.local.database.SearchDao
import ru.zarina.zarina.data.old.search.local.database.entity.SearchHistoryEntity

@Database(
    version = 4, exportSchema = true, entities = [
        CategoryEntity::class,
        SearchHistoryEntity::class,
    ]
)
@TypeConverters(TypeConverter::class)
abstract class ZarinaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun searchDao(): SearchDao

}
