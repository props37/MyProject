package ru.livetyping.zarina.data.auth.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import javax.inject.Inject

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val authEncryptedStorage: AuthEncryptedStorage,
) : AuthLocalDataSource {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        return authEncryptedStorage.getBearerTokensFlow()
    }

    override suspend fun setBearerTokens(tokens: BearerTokens?) {
        authEncryptedStorage.setBearerTokens(tokens)
    }
}
