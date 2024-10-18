package ru.livetyping.zarina.data.auth.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.data.auth.impl.local.AuthLocalDataSource
import ru.livetyping.zarina.data.auth.impl.remote.AuthRemoteDataSource
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        return localDataSource.getBearerTokensFlow()
    }

    override suspend fun setBearerTokens(tokens: BearerTokens?) {
        localDataSource.setBearerTokens(tokens)
    }

    override suspend fun refreshBearerTokens(oldTokens: BearerTokens): BearerTokens {
        return remoteDataSource.refreshBearerTokens(oldTokens)
    }

    override suspend fun getNewUnauthorizedUserBearerTokens(): BearerTokens {
        return remoteDataSource.getNewUnauthorizedUserBearerTokens()
    }
}
