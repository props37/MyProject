package ru.livetyping.zarina.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryQueryDao
import ru.livetyping.zarina.data.productsearch.local.database.entity.ProductSearchHistoryQueryEntity
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.data.user.local.database.entity.UserEntity

@Database(
    version = 2,
    entities = [UserEntity::class, ProductSearchHistoryQueryEntity::class],
    exportSchema = false,
)
abstract class ZarinaDatabaseOld : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getProductSearchHistoryQueryDao(): ProductSearchHistoryQueryDao
}
