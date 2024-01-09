package ru.zarina.zarina.data.rework.authorization.local

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.domain.rework.common.Token
import javax.inject.Inject

class AuthorizationEncryptedStorage @Inject constructor(
    @Qualifiers.SharedPreferences(Qualifiers.ShapredPreferencesType.ENCRYPTED)
    private val encryptedSharedPreferences: SharedPreferences,
) {
    suspend fun getAuthorizationTokens(): AuthorizationTokens? {
        return withContext(Dispatchers.IO) {
            val accessToken = encryptedSharedPreferences.getString(KEY_ACCESS_TOKEN, null)
            val refreshToken = encryptedSharedPreferences.getString(KEY_REFRESH_TOKEN, null)
            if (accessToken != null && refreshToken != null) {
                AuthorizationTokens(Token(accessToken), Token(refreshToken))
            } else {
                null
            }
        }
    }

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        withContext(Dispatchers.IO) {
            encryptedSharedPreferences.edit(commit = true) {
                putString(KEY_ACCESS_TOKEN, tokens?.accessToken?.value)
                putString(KEY_REFRESH_TOKEN, tokens?.refreshToken?.value)
            }
        }
    }

    suspend fun clear() {
        setAuthorizationTokens(null)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "authorization_access_token"
        private const val KEY_REFRESH_TOKEN = "authorization_refresh_token"
    }
}
