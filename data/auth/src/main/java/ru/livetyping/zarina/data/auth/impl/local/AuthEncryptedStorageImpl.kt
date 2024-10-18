package ru.livetyping.zarina.data.auth.impl.local

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.common.Token
import ru.livetyping.zarina.core.sharedpreferences.di.SharedPreferencesQualifier
import ru.livetyping.zarina.core.sharedpreferences.di.SharedPreferencesType
import timber.log.Timber
import javax.inject.Inject

internal class AuthEncryptedStorageImpl @Inject constructor(
    @SharedPreferencesQualifier(SharedPreferencesType.ENCRYPTED)
    private val encryptedSharedPreferences: SharedPreferences,
) : AuthEncryptedStorage {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        return getBearerTokensFlowImpl()
    }

    private fun getBearerTokensFlowImpl(): Flow<BearerTokens?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            if (key == KEY_ACCESS_TOKEN || key == KEY_REFRESH_TOKEN) {
                val tokens = preferences.getBearerTokens()
                Timber.tag(TAG).v("Bearer tokens changed: $tokens")
                trySend(tokens)
            }
        }
        encryptedSharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        val initialTokens = encryptedSharedPreferences.getBearerTokens()
        Timber.tag(TAG).v("Current Bearer tokens: $initialTokens")
        trySend(initialTokens)

        awaitClose {
            encryptedSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
        .buffer(capacity = Channel.CONFLATED)
        .flowOn(Dispatchers.IO) // TODO: [Low] Inject dispatcher

    override suspend fun setBearerTokens(tokens: BearerTokens?) {
        // TODO: [Low] Inject dispatcher
        withContext(Dispatchers.IO) {
            encryptedSharedPreferences.edit {
                putString(KEY_ACCESS_TOKEN, tokens?.accessToken?.value)
                putString(KEY_REFRESH_TOKEN, tokens?.refreshToken?.value)
            }
        }
        Timber.tag(TAG).v("Bearer tokens set: $tokens")
    }

    private fun SharedPreferences.getBearerTokens(): BearerTokens? {
        val accessToken = this.getString(KEY_ACCESS_TOKEN, null)
        val refreshToken = this.getString(KEY_REFRESH_TOKEN, null)
        return if (accessToken != null && refreshToken != null) {
            BearerTokens(Token(accessToken), Token(refreshToken))
        } else {
            null
        }
    }

    private companion object {
        private const val KEY_ACCESS_TOKEN = "auth_access_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"

        private const val TAG = "AuthEncryptedStorageImpl"
    }
}
