package ru.livetyping.zarina.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryEntryDao
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryEntryEntity
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.data.user.local.database.entity.UserEntity

@Database(
    version = 2,
    entities = [UserEntity::class, ProductSearchHistoryEntryEntity::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
abstract class ZarinaDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getProductSearchHistoryEntryDao(): ProductSearchHistoryEntryDao
}
