package ru.livetyping.zarina.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.utils.coroutine.catch
import java.io.IOException

operator fun <T> MutablePreferences.set(key: Preferences.Key<T>, value: T?) {
    if (value != null) this[key] = value else this.remove(key)
}

val <T> DataStore<T>.safeData: Flow<T>
    get() = this.data
        .catch<T, IOException> { }
