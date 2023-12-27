package ru.zarina.zarina.util.library.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

val DataStore<Preferences>.safeData: Flow<Preferences>
    get() = this.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }
