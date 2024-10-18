package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens

public interface AuthRepository {
    public fun getBearerTokensFlow(): Flow<BearerTokens?>

    public suspend fun setBearerTokens(tokens: BearerTokens?)

    public suspend fun refreshBearerTokens(oldTokens: BearerTokens?): BearerTokens
}
