package ru.zarina.zarina.usecase.rework.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.authorization.AuthorizationRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.utils.clean.invoke
import timber.log.Timber
import javax.inject.Inject

class UpdateUnauthorizedUserAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
    private val fetchUnauthorizedUserAuthorizationTokensUseCase: FetchUnauthorizedUserAuthorizationTokensUseCase,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        val currentTokens = authorizationRepository.getAuthorizationTokens()
        if (currentTokens == null) {
            fetchUnauthorizedUserAuthorizationTokensUseCase().getOrThrow()
        } else {
            Timber.v("Do not fetch tokens since the tokens are present")
        }
    }
}
