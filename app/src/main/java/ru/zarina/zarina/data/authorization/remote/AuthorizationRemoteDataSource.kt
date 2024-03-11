package ru.zarina.zarina.data.authorization.remote

import ru.zarina.zarina.data.authorization.remote.api.AuthorizationApi
import ru.zarina.zarina.domain.authorization.AuthorizationTokens
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
