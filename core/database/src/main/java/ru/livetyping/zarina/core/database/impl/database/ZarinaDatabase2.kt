package ru.livetyping.zarina.core.database.impl.database

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
internal abstract class ZarinaDatabase2 : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getSearchHistoryQueryDao(): SearchHistoryQueryDao
}
