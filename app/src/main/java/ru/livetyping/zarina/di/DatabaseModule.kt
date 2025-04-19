package ru.livetyping.zarina.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.database.ZarinaDatabase2
import ru.livetyping.zarina.core.database.ZarinaDatabaseCallback
import ru.livetyping.zarina.core.database.search.SearchHistoryQueryDao
import ru.livetyping.zarina.core.database.transaction.ZarinaDatabaseTransactionManager
import ru.livetyping.zarina.core.database.user.UserDao
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideZarinaDatabase(
        @ApplicationContext
        context: Context,
        onDestructiveMigration: ZarinaDatabaseCallback,
    ): ZarinaDatabase2 {
        return Room.databaseBuilder(
            context = context,
            klass = ZarinaDatabase2::class.java,
            name = ZarinaDatabase2.DATABASE_NAME,
        )
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

    @Provides
    @Singleton
    fun provideSearchHistoryQueryDao(database: ZarinaDatabase2): SearchHistoryQueryDao {
        return database.getSearchHistoryQueryDao()
    }

    @Provides
    fun provideZarinaDatabaseTransactionManager(
        database: ZarinaDatabase2,
    ): ZarinaDatabaseTransactionManager {
        return ZarinaDatabaseTransactionManager.createInstance(database)
    }

    @Provides
    fun provideZarinaDatabaseCallback(
        forcedSignOutCoordinator: ForcedSignOutCoordinator,
    ): ZarinaDatabaseCallback {
        return object : ZarinaDatabaseCallback {
            override fun onDestructiveMigration() {
                Timber.tag(ZARINA_DATABASE_CALLBACK_TAG).w("ZarinaDatabase was destructively migrated")
                forcedSignOutCoordinator.requestForcedSignOut()
            }
        }
    }

    companion object {
        private const val ZARINA_DATABASE_CALLBACK_TAG = "ZarinaDatabaseCallback"
    }
}
