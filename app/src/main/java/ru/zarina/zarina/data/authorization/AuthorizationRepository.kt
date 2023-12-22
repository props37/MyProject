package ru.zarina.zarina.data.authorization

import ru.zarina.zarina.data.authorization.remote.AuthorizationRemoteDataSource
import ru.zarina.zarina.domain.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val remoteDataSource: AuthorizationRemoteDataSource,
) {
    suspend fun getUnauthorizedUserAuthorizationTokens(): AuthorizationTokens {
        return remoteDataSource.getUnauthorizedUserAuthorizationTokens()
    }
}
