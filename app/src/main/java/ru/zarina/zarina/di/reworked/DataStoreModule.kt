package ru.zarina.zarina.di.reworked

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE_NAME)

    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<Preferences> {
        return context.preferencesDataStore
    }

    companion object {
        private const val PREFERENCES_DATA_STORE_NAME = "preferences_data_store"
    }
}
