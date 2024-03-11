package ru.zarina.zarina.di.old

import android.content.Context
import androidx.room.Room
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.data.old.database.TypeConverter
import ru.zarina.zarina.data.old.database.ZarinaDatabase


@Module
class DatabaseModule {

    @Factory
    fun provideTypeConverter(
        json: Json,
    ) = TypeConverter(json)

    @Singleton
    fun provideZarinaDatabase(
        context: Context,
        converter: TypeConverter,
    ): ZarinaDatabase {
        return Room
            .databaseBuilder(context, ZarinaDatabase::class.java, "zarina.db")
            .addTypeConverter(converter)
            .build()
    }

    @Factory
    fun provideCategoryDao(
        database: ZarinaDatabase,
    ) = database.categoryDao()

    @Factory
    fun provideSearchDao(
        database: ZarinaDatabase,
    ) = database.searchDao()

}
