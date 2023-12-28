package ru.zarina.zarina.usecase.rework.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.authorization.AuthorizationRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class GetAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : UseCase<Unit, AuthorizationTokens?>(dispatcher) {

    override suspend fun execute(params: Unit): AuthorizationTokens? {
        return authorizationRepository.getAuthorizationTokens()
    }
}
