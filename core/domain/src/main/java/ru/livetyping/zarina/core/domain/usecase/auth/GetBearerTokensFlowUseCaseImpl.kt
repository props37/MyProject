package ru.livetyping.zarina.core.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetBearerTokensFlowUseCaseImpl(
    private val authRepository: AuthRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, BearerTokens?>(logger), GetBearerTokensFlowUseCase {

    override fun execute(params: Unit): Flow<BearerTokens?> {
        return authRepository.getBearerTokensFlow()
    }

    override fun invoke(): Flow<Result<BearerTokens?>> {
        return call(Unit)
    }
}
