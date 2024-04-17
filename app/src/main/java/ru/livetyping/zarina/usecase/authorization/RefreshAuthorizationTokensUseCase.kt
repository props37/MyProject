package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

// TODO: [Medium] Is synchronization needed?

class RefreshAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
    private val fetchUnauthorizedUserAuthorizationTokensUseCase: FetchUnauthorizedUserAuthorizationTokensUseCase,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Refresh authorization tokens")
        val currentTokens = authorizationRepository.getAuthorizationTokensFlow().firstOrNull()
        if (currentTokens != null) {
            val newTokens = authorizationRepository.refreshAuthorizationTokens(currentTokens)
            authorizationRepository.setAuthorizationTokens(newTokens)
            Timber.v("Authorization tokens refreshed. New tokens: $newTokens")
        } else {
            fetchUnauthorizedUserAuthorizationTokensUseCase()
        }
    }
}
