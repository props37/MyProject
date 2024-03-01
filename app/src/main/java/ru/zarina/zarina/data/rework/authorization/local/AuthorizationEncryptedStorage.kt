package ru.zarina.zarina.data.rework.authorization.local

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.domain.rework.common.Token
import timber.log.Timber
import javax.inject.Inject

class AuthorizationEncryptedStorage @Inject constructor(
    @Qualifiers.SharedPreferences(Qualifiers.ShapredPreferencesType.ENCRYPTED)
    private val encryptedSharedPreferences: SharedPreferences,
) {
    fun getAuthorizationTokensFlow(): Flow<AuthorizationTokens?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sf, key ->
            if (key == KEY_ACCESS_TOKEN || key == KEY_REFRESH_TOKEN) {
                val tokens = sf.getAuthorizationTokens()
                Timber.v("Authorization tokens: $tokens")
                trySend(tokens)
            }
        }
        encryptedSharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        val initialTokens = encryptedSharedPreferences.getAuthorizationTokens()
        Timber.v("Authorization tokens: $initialTokens")
        trySend(initialTokens)

        awaitClose {
            encryptedSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
        .buffer(capacity = Channel.CONFLATED)

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        Timber.v("Set authorization tokens: $tokens")
        withContext(Dispatchers.IO) {
            encryptedSharedPreferences.edit(commit = true) {
                putString(KEY_ACCESS_TOKEN, tokens?.accessToken?.value)
                putString(KEY_REFRESH_TOKEN, tokens?.refreshToken?.value)
            }
        }
    }

    suspend fun clear() {
        Timber.v("Clear authorization tokens")
        setAuthorizationTokens(null)
    }

    private fun SharedPreferences.getAuthorizationTokens(): AuthorizationTokens? {
        val accessToken = this.getString(KEY_ACCESS_TOKEN, null)
        val refreshToken = this.getString(KEY_REFRESH_TOKEN, null)
        return if (accessToken != null && refreshToken != null) {
            AuthorizationTokens(Token(accessToken), Token(refreshToken))
        } else {
            null
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "authorization_access_token"
        private const val KEY_REFRESH_TOKEN = "authorization_refresh_token"
    }
}
