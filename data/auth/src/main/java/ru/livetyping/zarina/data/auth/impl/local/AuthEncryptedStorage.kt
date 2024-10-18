package ru.livetyping.zarina.data.auth.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens

internal interface AuthEncryptedStorage {
    fun getBearerTokensFlow(): Flow<BearerTokens?>

    suspend fun setBearerTokens(tokens: BearerTokens?)
}
