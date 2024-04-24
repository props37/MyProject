package ru.livetyping.zarina.usecase.authorization

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.data.common.remote.ktor.HttpClientAuthorizationTokensCleaner
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class ClearAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val authorizationRepository: AuthorizationRepository,
    private val httpClientAuthorizationTokensCleaner: HttpClientAuthorizationTokensCleaner,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Clear authorization tokens")
        httpClientAuthorizationTokensCleaner.clearAuthorizationTokens()
        authorizationRepository.clear()
    }
}
