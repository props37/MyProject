package ru.livetyping.zarina.data.auth.impl.remote.api

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.data.auth.impl.remote.api.dto.BearerTokensDto

internal interface AuthApi {
    suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokensDto
}
