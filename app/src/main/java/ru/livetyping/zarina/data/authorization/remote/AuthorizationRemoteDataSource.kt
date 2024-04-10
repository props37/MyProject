package ru.livetyping.zarina.data.authorization.remote

import ru.livetyping.zarina.data.authorization.remote.api.AuthorizationApi
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationRemoteDataSource @Inject constructor(
    private val api: AuthorizationApi,
) {
    suspend fun getUnauthorizedUserAuthorizationTokens(): AuthorizationTokens {
        return api.getUnauthorizedUserAuthorizationTokens().toAuthorizationTokens()
    }

    suspend fun refreshAuthorizationTokens(tokens: AuthorizationTokens): AuthorizationTokens {
        return api.refreshAuthorizationTokens(tokens).toAuthorizationTokens()
    }
}
