package ru.livetyping.zarina.di

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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.data.user.local.entity.CityEntity
import ru.livetyping.zarina.util.library.datastore.DataStoreSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE_NAME)

    @Provides
    @Singleton
    fun provideUserCityDataStore(
        @ApplicationContext
        context: Context,
        serializer: DataStoreSerializer<CityEntity?>,
    ): DataStore<CityEntity?> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile(USER_CITY_DATA_STORE_NAME) },
        )
    }

    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<Preferences> {
        return context.preferencesDataStore
    }

    @Provides
    fun provideUserCitySerializer(
        json: Json,
    ): DataStoreSerializer<CityEntity?> {
        return DataStoreSerializer(
            defaultValueProducer = { null },
            decodeFromString = { json.decodeFromString(it) },
            encodeToString = { json.encodeToString(it) },
        )
    }

    companion object {
        private const val PREFERENCES_DATA_STORE_NAME = "preferences_data_store"
        private const val USER_CITY_DATA_STORE_NAME = "user_city_data_store"
    }
}
