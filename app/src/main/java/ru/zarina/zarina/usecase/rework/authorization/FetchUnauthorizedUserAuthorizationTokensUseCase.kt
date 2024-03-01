package ru.zarina.zarina.usecase.rework.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.zarina.zarina.data.rework.authorization.AuthorizationRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.usecase.base.UseCase
import timber.log.Timber
import javax.inject.Inject

class FetchUnauthorizedUserAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        val currentTokens = authorizationRepository.getAuthorizationTokensFlow().firstOrNull()
        if (currentTokens == null) {
            val tokens = authorizationRepository.getNewUnauthorizedUserAuthorizationTokens()
            authorizationRepository.setAuthorizationTokens(tokens)
            Timber.v("Unauthorized user authorization tokens fetched")
        } else {
            Timber.v("No need to fetch authorization tokens since the tokens are present")
        }
    }
}
