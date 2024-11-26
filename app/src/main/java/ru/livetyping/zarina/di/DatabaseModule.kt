package ru.livetyping.zarina.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.livetyping.zarina.data.database.ZarinaDatabaseOld
import ru.livetyping.zarina.data.productsearch.local.database.dao.ProductSearchHistoryQueryDao
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.usecase.user.ForcedSignOutUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
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
        coroutineScope: CoroutineScope,
        forcedSignOutUseCase: Lazy<ForcedSignOutUseCase>,
    ): ZarinaDatabaseOld {
        return Room.databaseBuilder(context, ZarinaDatabaseOld::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    Timber.tag(DATABASE_NAME).w("onDestructiveMigration")
                    coroutineScope.launch {
                        forcedSignOutUseCase.get().invoke()
                    }
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: ZarinaDatabaseOld): UserDao {
        return database.getUserDao()
    }

    @Provides
    @Singleton
    fun provideProductSearchHistoryQueryDao(database: ZarinaDatabaseOld): ProductSearchHistoryQueryDao {
        return database.getProductSearchHistoryQueryDao()
    }

    companion object {
        private const val DATABASE_NAME = "zarina_database_old"
    }
}
