package ru.livetyping.zarina.data.auth.impl.remote

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.data.auth.impl.remote.api.AuthApi
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRemoteDataSource {
    override suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokens {
        val dto = api.refreshBearerTokens(oldTokens)
        return dto.toBearerTokens()
    }

    override suspend fun getNewUnauthorizedUserBearerTokens(): BearerTokens {
        val dto = api.getNewUnauthorizedUserAuthorizationTokens()
        return dto.toBearerTokens()
    }
}
