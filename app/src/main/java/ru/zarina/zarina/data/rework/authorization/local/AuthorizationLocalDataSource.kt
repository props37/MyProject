package ru.zarina.zarina.data.rework.authorization.local

import ru.zarina.zarina.domain.rework.AuthorizationTokens
import javax.inject.Inject

class AuthorizationLocalDataSource @Inject constructor(
    private val authorizationEncryptedStorage: AuthorizationEncryptedStorage,
) {
    suspend fun getAuthorizationTokens(): AuthorizationTokens? {
        return authorizationEncryptedStorage.getAuthorizationTokens()
    }

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        authorizationEncryptedStorage.setAuthorizationTokens(tokens)
    }

    suspend fun clear() {
        authorizationEncryptedStorage.clear()
    }
}
