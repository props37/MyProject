package ru.livetyping.zarina.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.database.ZarinaDatabaseCallback
import ru.livetyping.zarina.core.database.impl.database.ZarinaDatabase2
import ru.livetyping.zarina.core.database.impl.transaction.ZarinaDatabaseTransactionManagerImpl
import ru.livetyping.zarina.core.database.transaction.ZarinaDatabaseTransactionManager
import ru.livetyping.zarina.core.database.user.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatabaseModule {

    @Binds
    abstract fun bindZarinaDatabaseTransactionManager(
        impl: ZarinaDatabaseTransactionManagerImpl
    ): ZarinaDatabaseTransactionManager

    companion object {
        @Provides
        @Singleton
        fun provideZarinaDatabase(
            @ApplicationContext
            context: Context,
            onDestructiveMigration: ZarinaDatabaseCallback,
        ): ZarinaDatabase2 {
            return Room.databaseBuilder(context, ZarinaDatabase2::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        onDestructiveMigration.onDestructiveMigration()
                    }
                })
                .build()
        }

        @Provides
        @Singleton
        fun provideUserDao(database: ZarinaDatabase2): UserDao {
            return database.getUserDao()
        }

        private const val DATABASE_NAME = "zarina_database_2"
    }
}
