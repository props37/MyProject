package ru.livetyping.zarina.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataStoreModule {
    private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE_NAME)

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<Preferences> {
        return context.preferencesDataStore
    }

    private companion object {
        private const val PREFERENCES_DATA_STORE_NAME = "preferences_data_store_2"
    }
}
