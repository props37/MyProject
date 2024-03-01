package ru.zarina.zarina.usecase.rework.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.authorization.AuthorizationRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.usecase.base.UseCase
import ru.zarina.zarina.utils.clean.invoke
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
        val currentTokens = authorizationRepository.getAuthorizationTokens()
        if (currentTokens != null) {
            val newTokens = authorizationRepository.refreshAuthorizationTokens(currentTokens)
            authorizationRepository.setAuthorizationTokens(newTokens)
            Timber.v("Authorization tokens refreshed")
        } else {
            fetchUnauthorizedUserAuthorizationTokensUseCase()
        }
    }
}
