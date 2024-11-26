package ru.livetyping.zarina.data.auth.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.network.auth.BearerTokenCleaner
import ru.livetyping.zarina.data.auth.impl.local.storage.AuthEncryptedStorage
import javax.inject.Inject

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val authEncryptedStorage: AuthEncryptedStorage,
    private val bearerTokenCleaner: BearerTokenCleaner,
) : AuthLocalDataSource {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        return authEncryptedStorage.getBearerTokensFlow()
    }

    override suspend fun setBearerTokens(tokens: BearerTokens?) {
        bearerTokenCleaner.clearBearerTokens()
        authEncryptedStorage.setBearerTokens(tokens)
    }
}
