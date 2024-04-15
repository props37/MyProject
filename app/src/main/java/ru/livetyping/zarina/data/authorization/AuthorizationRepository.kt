package ru.livetyping.zarina.data.authorization

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.authorization.local.AuthorizationLocalDataSource
import ru.livetyping.zarina.data.authorization.remote.AuthorizationRemoteDataSource
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val localDataSource: AuthorizationLocalDataSource,
    private val remoteDataSource: AuthorizationRemoteDataSource,
) {
    fun getAuthorizationTokensFlow(): Flow<AuthorizationTokens?> {
        return localDataSource.getAuthorizationTokensFlow()
    }

    fun setAuthorizationTokens(tokens: AuthorizationTokens?) {
        localDataSource.setAuthorizationTokens(tokens)
    }

    suspend fun getNewUnauthorizedUserAuthorizationTokens(): AuthorizationTokens {
        return remoteDataSource.getUnauthorizedUserAuthorizationTokens()
    }

    suspend fun refreshAuthorizationTokens(tokens: AuthorizationTokens): AuthorizationTokens {
        return remoteDataSource.refreshAuthorizationTokens(tokens)
    }

    fun clear() {
        localDataSource.clear()
    }
}
