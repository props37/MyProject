package ru.livetyping.zarina.data.auth.impl.local.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.datastore.safeData
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.common.Token
import timber.log.Timber
import javax.inject.Inject

internal class AuthEncryptedStorageImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
) : AuthEncryptedStorage {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        return getBearerTokensFlowImpl()
    }

    private fun getBearerTokensFlowImpl(): Flow<BearerTokens?> {
        return preferencesDataStore.safeData.map { data ->
            val accessToken = data[KEY_ACCESS_TOKEN]
            val refreshToken = data[KEY_REFRESH_TOKEN]
            if (accessToken != null && refreshToken != null) {
                BearerTokens(Token(accessToken), Token(refreshToken))
            } else {
                null
            }
        }
    }

    override suspend fun setBearerTokens(tokens: BearerTokens?) {
        preferencesDataStore.edit { data ->
            if (tokens != null) {
                data[KEY_ACCESS_TOKEN] = tokens.accessToken.value
                data[KEY_REFRESH_TOKEN] = tokens.refreshToken.value
            } else {
                data.remove(KEY_ACCESS_TOKEN)
                data.remove(KEY_REFRESH_TOKEN)
            }
        }
        Timber.tag(TAG).v("Bearer tokens set: $tokens")
    }

    override suspend fun clear() {
        setBearerTokens(null)
        Timber.tag(TAG).v("Bearer tokens cleared")
    }

    private companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("auth_access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("auth_refresh_token")

        private const val TAG = "AuthEncryptedStorageImpl"
    }
}
