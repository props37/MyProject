package ru.livetyping.zarina.data.auth.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.data.auth.impl.local.AuthLocalDataSource
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
) : AuthRepository {
    override fun getBearerTokensFlow(): Flow<BearerTokens?> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshBearerTokens(oldTokens: BearerTokens?): BearerTokens {
        // TODO: [Top] Implement
        TODO("Not yet implemented")
    }
}
