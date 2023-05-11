package ru.zarina.zarina.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.data.user.local.entity.CityDataEntity
import ru.zarina.zarina.utils.datastore.Serializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

    @Provides
    @Singleton
    fun providesPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.dataStore


    @Provides
    fun provideUserCitySerializer(
        json: Json,
    ) = Serializer<CityDataEntity?>(
        defaultValueProducer = { null },
        decodeFromString = { json.decodeFromString(it) },
        encodeToString = { json.encodeToString(it) }
    )

    @Provides
    @Singleton
    fun providesUserCityDataStore(
        @ApplicationContext context: Context,
        serializer: Serializer<CityDataEntity?>,
    ): DataStore<CityDataEntity?> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile(USER_CITY_DATA_STORE_NAME) },
        )
    }

    companion object {
        private const val DATA_STORE_NAME = "zarina-main"
        private const val USER_CITY_DATA_STORE_NAME = "zarina-user-city"
    }

}
