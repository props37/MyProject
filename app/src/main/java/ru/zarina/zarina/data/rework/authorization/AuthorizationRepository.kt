package ru.zarina.zarina.data.rework.authorization

import ru.zarina.zarina.data.rework.authorization.local.AuthorizationLocalDataSource
import ru.zarina.zarina.data.rework.authorization.remote.AuthorizationRemoteDataSource
import ru.zarina.zarina.domain.rework.AuthorizationTokens
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val localDataSource: AuthorizationLocalDataSource,
    private val remoteDataSource: AuthorizationRemoteDataSource,
) {
    suspend fun getAuthorizationTokens(): AuthorizationTokens? {
        return localDataSource.getAuthorizationTokens()
    }

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        localDataSource.setAuthorizationTokens(tokens)
    }

    suspend fun requestUnauthorizedUserAuthorizationTokens(): AuthorizationTokens {
        return remoteDataSource.getUnauthorizedUserAuthorizationTokens()
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
