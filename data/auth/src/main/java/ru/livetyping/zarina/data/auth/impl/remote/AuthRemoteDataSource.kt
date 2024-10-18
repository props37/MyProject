package ru.livetyping.zarina.data.auth.impl.remote

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens

internal interface AuthRemoteDataSource {
    suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokens

    suspend fun getNewUnauthorizedUserBearerTokens(): BearerTokens
}
