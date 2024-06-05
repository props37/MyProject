package ru.livetyping.zarina.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.data.database.ZarinaDatabase
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryEntryDao
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideZarinaDatabase(
        @ApplicationContext
        context: Context,
    ): ZarinaDatabase {
        return Room.databaseBuilder(context, ZarinaDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: ZarinaDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    @Singleton
    fun provideProductSearchHistoryEntryDao(database: ZarinaDatabase): ProductSearchHistoryEntryDao {
        return database.getProductSearchHistoryEntryDao()
    }

    companion object {
        private const val DATABASE_NAME = "zarina_database"
    }
}
