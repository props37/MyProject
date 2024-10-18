package ru.livetyping.zarina.core.domain.usecase.auth

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface FetchUnauthorizedUserBearerTokensUseCase {
    public suspend operator fun invoke(): Result<BearerTokens>

    public companion object {
        public fun getInstance(
            authRepository: AuthRepository,
            logger: UseCaseLogger?,
        ): FetchUnauthorizedUserBearerTokensUseCase {
            return FetchUnauthorizedUserBearerTokensUseCaseImpl(
                authRepository = authRepository,
                logger = logger,
            )
        }
    }
}
