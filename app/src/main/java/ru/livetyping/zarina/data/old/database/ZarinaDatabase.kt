package ru.livetyping.zarina.data.old.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.livetyping.zarina.data.old.category.local.database.CategoryDao
import ru.livetyping.zarina.data.old.category.local.database.entity.CategoryEntity
import ru.livetyping.zarina.data.old.search.local.database.SearchDao
import ru.livetyping.zarina.data.old.search.local.database.entity.SearchHistoryEntity

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
