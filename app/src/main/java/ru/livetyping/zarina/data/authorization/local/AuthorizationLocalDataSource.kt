package ru.livetyping.zarina.data.authorization.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationLocalDataSource @Inject constructor(
    private val authorizationEncryptedStorage: AuthorizationEncryptedStorage,
) {
    fun getAuthorizationTokensFlow(): Flow<AuthorizationTokens?> {
        return authorizationEncryptedStorage.getAuthorizationTokensFlow()
    }

    fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        authorizationEncryptedStorage.setAuthorizationTokens(tokens)
    }

    fun clear() {
        authorizationEncryptedStorage.clear()
    }
}
