package ru.livetyping.zarina.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.livetyping.zarina.core.database.search.SearchHistoryQueryDao
import ru.livetyping.zarina.core.database.search.SearchHistoryQueryEntity
import ru.livetyping.zarina.core.database.user.UserDao
import ru.livetyping.zarina.core.database.user.UserEntity

@Database(
    version = 2,
    entities = [UserEntity::class, SearchHistoryQueryEntity::class],
)
public abstract class ZarinaDatabase2 : RoomDatabase() {
    public abstract fun getUserDao(): UserDao
    public abstract fun getSearchHistoryQueryDao(): SearchHistoryQueryDao

    public companion object {
        public const val DATABASE_NAME: String = "zarina_database_2"
    }
}
