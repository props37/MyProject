package ru.livetyping.zarina.core.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public class GetBearerTokensFlowUseCase(
    private val authRepository: AuthRepository,
    userCaseLogger: UseCaseLogger?,
) : FlowUseCase<Unit, BearerTokens?>(userCaseLogger) {

    override fun execute(params: Unit): Flow<BearerTokens?> {
        return authRepository.getBearerTokensFlow()
    }
}
