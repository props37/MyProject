package ru.zarina.zarina.usecase.rework.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.rework.authorization.AuthorizationRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class FetchUnauthorizedUserAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        val currentTokens = authorizationRepository.getAuthorizationTokens()
        if (currentTokens == null) {
            val tokens = authorizationRepository.requestUnauthorizedUserAuthorizationTokens()
            authorizationRepository.setAuthorizationTokens(tokens)
            Timber.v("Unauthorized user authorization tokens fetched")
        } else {
            Timber.v("No need to fetch authorization tokens since the tokens are present")
        }
    }
}
