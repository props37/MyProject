package ru.zarina.zarina.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.utils.coroutine.catch
import java.io.IOException

operator fun <T> MutablePreferences.set(key: Preferences.Key<T>, value: T?) {
    if (value != null) this[key] = value else this.remove(key)
}

val DataStore<Preferences>.safeData: Flow<Preferences>
    get() = this.data
        .catch<Preferences, IOException> { emit(emptyPreferences()) }
