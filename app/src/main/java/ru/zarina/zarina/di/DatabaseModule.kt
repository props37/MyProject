package ru.zarina.zarina.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import ru.zarina.zarina.data.base.database.TypeConverter
import ru.zarina.zarina.data.base.database.ZarinaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideTypeConverter(
        json: Json,
    ) = TypeConverter(json)

    @Singleton
    @Provides
    fun provideZarinaDatabase(
        @ApplicationContext context: Context,
        converter: TypeConverter,
    ): ZarinaDatabase {
        return Room
            .databaseBuilder(context, ZarinaDatabase::class.java, "zarina.db")
            .addTypeConverter(converter)
            .build()
    }

    @Provides
    fun provideCategoryDao(
        database: ZarinaDatabase,
    ) = database.categoryDao()

}
