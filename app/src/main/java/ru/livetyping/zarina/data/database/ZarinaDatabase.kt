package ru.livetyping.zarina.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.data.user.local.database.entity.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class],
)
abstract class ZarinaDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}
