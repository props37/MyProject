package ru.zarina.zarina.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.zarina.zarina.data.ZarinaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideZarinaDatabase(
        @ApplicationContext context: Context,
    ): ZarinaDatabase {
        return Room.databaseBuilder(context, ZarinaDatabase::class.java, "zarina.db").build()
    }

    @Provides
    fun provideCategoryDao(
        database: ZarinaDatabase,
    ) = database.categoryDao()

}
