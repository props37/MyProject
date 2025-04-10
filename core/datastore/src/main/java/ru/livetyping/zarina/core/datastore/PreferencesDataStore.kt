package ru.livetyping.zarina.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

private const val PREFERENCES_DATA_STORE_NAME = "preferences_data_store"

public val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_DATA_STORE_NAME)

public val DataStore<Preferences>.safeData: Flow<Preferences>
    get() = this.data.catch { t ->
        if (t is IOException) emit(emptyPreferences()) else throw t
    }
