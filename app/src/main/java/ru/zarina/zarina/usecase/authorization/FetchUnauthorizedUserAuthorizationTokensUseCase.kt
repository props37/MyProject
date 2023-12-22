package ru.zarina.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.authorization.AuthorizationRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import javax.inject.Inject

class FetchUnauthorizedUserAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        val tokens = authorizationRepository.getUnauthorizedUserAuthorizationTokens()
        authorizationRepository.setAuthorizationTokens(tokens)
    }
}
