package ru.zarina.zarina.data.rework.authorization

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.authorization.local.AuthorizationLocalDataSource
import ru.zarina.zarina.data.rework.authorization.remote.AuthorizationRemoteDataSource
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val localDataSource: AuthorizationLocalDataSource,
    private val remoteDataSource: AuthorizationRemoteDataSource,
) {
    fun getAuthorizationTokensFlow(): Flow<AuthorizationTokens?> {
        return localDataSource.getAuthorizationTokensFlow()
    }

    suspend fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        localDataSource.setAuthorizationTokens(tokens)
    }

    suspend fun getNewUnauthorizedUserAuthorizationTokens(): AuthorizationTokens {
        return remoteDataSource.getUnauthorizedUserAuthorizationTokens()
    }

    suspend fun refreshAuthorizationTokens(tokens: AuthorizationTokens): AuthorizationTokens {
        return remoteDataSource.refreshAuthorizationTokens(tokens)
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}
