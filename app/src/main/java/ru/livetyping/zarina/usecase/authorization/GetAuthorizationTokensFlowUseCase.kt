package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
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
