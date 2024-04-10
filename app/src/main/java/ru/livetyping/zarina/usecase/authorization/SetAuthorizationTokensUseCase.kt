package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.data.common.remote.ktor.HttpClientAuthorizationTokensCleaner
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import timber.log.Timber
import javax.inject.Inject

class SetAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
    private val httpClientAuthorizationTokensCleaner: HttpClientAuthorizationTokensCleaner,
) : UseCase<SetAuthorizationTokensUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val tokens = params.tokens
        Timber.v("Set authorization tokens: $tokens")
        authorizationRepository.setAuthorizationTokens(tokens)
        httpClientAuthorizationTokensCleaner.clearAuthorizationTokens()
    }

    data class Params(val tokens: AuthorizationTokens)
}
