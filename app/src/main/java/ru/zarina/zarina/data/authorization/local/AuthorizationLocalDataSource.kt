package ru.zarina.zarina.data.authorization.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationLocalDataSource @Inject constructor(
    private val authorizationEncryptedStorage: AuthorizationEncryptedStorage,
) {
    fun getAuthorizationTokensFlow(): Flow<AuthorizationTokens?> {
        return authorizationEncryptedStorage.getAuthorizationTokensFlow()
    }

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        authorizationEncryptedStorage.setAuthorizationTokens(tokens)
    }

    suspend fun clear() {
        authorizationEncryptedStorage.clear()
    }
}
