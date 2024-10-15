package ru.livetyping.zarina.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

public val DataStore<Preferences>.safeData: Flow<Preferences>
    get() = this.data.catch { t ->
        if (t is IOException) emit(emptyPreferences()) else throw t
    }
