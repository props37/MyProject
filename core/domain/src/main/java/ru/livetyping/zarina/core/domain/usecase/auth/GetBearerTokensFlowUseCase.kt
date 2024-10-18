package ru.livetyping.zarina.core.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetBearerTokensFlowUseCase {
    public operator fun invoke(): Flow<Result<BearerTokens?>>

    public companion object {
        public fun getInstance(
            authRepository: AuthRepository,
            logger: UseCaseLogger?,
        ): GetBearerTokensFlowUseCase {
            return GetBearerTokensFlowUseCaseImpl(authRepository, logger)
        }
    }
}
