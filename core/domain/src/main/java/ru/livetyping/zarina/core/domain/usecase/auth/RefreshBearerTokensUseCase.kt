package ru.livetyping.zarina.core.domain.usecase.auth

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RefreshBearerTokensUseCase {
    public suspend operator fun invoke(params: Params): Result<BearerTokens>

    public data class Params(val oldTokens: BearerTokens?)

    public companion object {
        public fun getInstance(
            authRepository: AuthRepository,
            logger: UseCaseLogger?,
        ): RefreshBearerTokensUseCase {
            return RefreshBearerTokensUseCaseImpl(
                authRepository = authRepository,
                logger = logger,
            )
        }
    }
}
