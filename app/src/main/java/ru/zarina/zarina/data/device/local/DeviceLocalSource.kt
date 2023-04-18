package ru.zarina.zarina.data.device.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.domain.AuthorizationToken
import ru.zarina.zarina.utils.datastore.safeData
import ru.zarina.zarina.utils.datastore.set
import javax.inject.Inject

class DeviceLocalSource @Inject constructor(
    private val store: DataStore<Preferences>,
) : IDeviceLocalSource {

    override fun getToken(): Flow<AuthorizationToken.Device?> {
        return store.safeData
            .map { preferences ->
                val tokenString = preferences[KEY_DEVICE_TOKEN]
                tokenString?.let { AuthorizationToken.Device(it) }
            }
    }

    override suspend fun setToken(token: AuthorizationToken.Device?) {
        store.edit { preferences ->
            preferences[KEY_DEVICE_TOKEN] = token?.token
        }
    }

    companion object {
        private val KEY_DEVICE_TOKEN = stringPreferencesKey("device_token")
    }

}
