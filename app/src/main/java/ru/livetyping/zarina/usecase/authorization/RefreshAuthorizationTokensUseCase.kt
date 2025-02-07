package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.data.signout.ForcedSignOutCoordinator
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class RefreshAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
    private val fetchUnauthorizedUserAuthorizationTokensUseCase: FetchUnauthorizedUserAuthorizationTokensUseCase,
    private val forcedSignOutCoordinator: ForcedSignOutCoordinator,
) : UseCase<Unit, AuthorizationTokens>(dispatcher) {

    override suspend fun execute(params: Unit): AuthorizationTokens {
        Timber.v("Refresh authorization tokens")
        val currentTokens = authorizationRepository.getAuthorizationTokensFlow().firstOrNull()
        return if (currentTokens != null) {
            refreshTokens(currentTokens)
        } else {
            fetchUnauthorizedUserAuthorizationTokensUseCase().getOrThrow()
        }
    }

    private suspend fun refreshTokens(currentTokens: AuthorizationTokens): AuthorizationTokens {
        return try {
            val newTokens = authorizationRepository.refreshAuthorizationTokens(currentTokens)
            authorizationRepository.setAuthorizationTokens(newTokens)
            Timber.v("Authorization tokens refreshed. New tokens: $newTokens")
            newTokens
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh authorization tokens. Request forced sign out")
            forcedSignOutCoordinator.requestForcedSignOut()
            fetchUnauthorizedUserAuthorizationTokensUseCase()
            throw e
        }
    }
}
