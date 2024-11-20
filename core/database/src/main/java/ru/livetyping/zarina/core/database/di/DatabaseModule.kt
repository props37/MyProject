package ru.livetyping.zarina.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.livetyping.zarina.core.database.database.ZarinaDatabase
import ru.livetyping.zarina.core.database.user.UserDao
import javax.inject.Singleton

@Module
@InstallIn
internal class DatabaseModule {

    @Provides
    @Singleton
    fun provideZarinaDatabase(
        @ApplicationContext
        context: Context,
    ): ZarinaDatabase {
        return Room.databaseBuilder(context, ZarinaDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    // TODO: [Top] Implement
                    super.onDestructiveMigration(db)
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: ZarinaDatabase): UserDao {
        return database.getUserDao()
    }

    private companion object {
        private const val DATABASE_NAME = "zarina_database"
    }
}
