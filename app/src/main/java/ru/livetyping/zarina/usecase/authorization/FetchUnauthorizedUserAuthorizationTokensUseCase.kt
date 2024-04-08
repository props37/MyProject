package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class FetchUnauthorizedUserAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Fetch unauthorized user authorization tokens")
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
