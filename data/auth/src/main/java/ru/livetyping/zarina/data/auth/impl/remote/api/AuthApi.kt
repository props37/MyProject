package ru.livetyping.zarina.data.auth.impl.remote.api

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.network.zarina.dto.BearerTokensDto

internal interface AuthApi {
    suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokensDto

    suspend fun getNewUnauthorizedUserAuthorizationTokens(): BearerTokensDto
}
