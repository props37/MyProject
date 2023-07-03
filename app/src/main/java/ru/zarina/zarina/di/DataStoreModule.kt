package ru.zarina.zarina.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.data.user.local.entity.CityDataEntity
import ru.zarina.zarina.utils.datastore.Serializer

@Module
class DataStoreModule {

    private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

    @Singleton
    @Named(Qualifiers.DataStore.PREFERENCES)
    fun providesPreferencesDataStore(
        context: Context,
    ): DataStore<Preferences> = context.dataStore

    @Factory
    fun provideUserCitySerializer(
        json: Json,
    ) = Serializer<CityDataEntity?>(
        defaultValueProducer = { null },
        decodeFromString = { json.decodeFromString(it) },
        encodeToString = { json.encodeToString(it) }
    )

    @Singleton
    @Named(Qualifiers.DataStore.USER_CITY)
    fun providesUserCityDataStore(
        context: Context,
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
