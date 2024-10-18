package ru.livetyping.zarina.core.domain.usecase.auth

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public class RefreshBearerTokensUseCase(
    private val authRepository: AuthRepository,
    useCaseLogger: UseCaseLogger?,
) : UseCase<Params, BearerTokens?>(useCaseLogger) {

    override suspend fun execute(params: Params): BearerTokens? {
        val oldTokens = params.oldTokens
        // TODO: [Top] Implement
        TODO()
    }

    public data class Params(val oldTokens: BearerTokens?)
}
