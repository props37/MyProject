package ru.zarina.zarina.data.device.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.AuthorizationToken
import ru.zarina.zarina.utils.datastore.safeData
import ru.zarina.zarina.utils.datastore.set

@Factory
class DeviceLocalSource(
    @Named(Qualifiers.DataStore.PREFERENCES)
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

    override fun getIsOnboardingCompleted(): Flow<Boolean> {
        return store.safeData
            .map { preferences ->
                preferences[KEY_IS_ONBOARDING_COMPLETED] == true
            }
    }

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) {
        store.edit { preferences ->
            preferences[KEY_IS_ONBOARDING_COMPLETED] = isOnboardingCompleted
        }
    }

    companion object {
        private val KEY_DEVICE_TOKEN = stringPreferencesKey("device_token")
        private val KEY_IS_ONBOARDING_COMPLETED =
            booleanPreferencesKey("device_is_onboarding_completed")
    }

}
