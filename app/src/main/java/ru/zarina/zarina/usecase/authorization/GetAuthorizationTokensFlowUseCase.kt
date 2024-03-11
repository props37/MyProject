package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.authorization.AuthorizationRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import javax.inject.Inject

class GetAuthorizationTokensFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : FlowUseCase<Unit, AuthorizationTokens?>(dispatcher) {

    override fun execute(params: Unit): Flow<AuthorizationTokens?> {
        return authorizationRepository.getAuthorizationTokensFlow()
    }
}
