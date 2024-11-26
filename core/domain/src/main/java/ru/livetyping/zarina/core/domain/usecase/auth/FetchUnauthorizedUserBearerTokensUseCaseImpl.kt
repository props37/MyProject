package ru.livetyping.zarina.core.domain.usecase.auth

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class FetchUnauthorizedUserBearerTokensUseCaseImpl(
    private val authRepository: AuthRepository,
    private val logger: UseCaseLogger?,
) : UseCase<Unit, BearerTokens>(logger), FetchUnauthorizedUserBearerTokensUseCase {

    override suspend fun execute(params: Unit): BearerTokens {
        val currentTokens = authRepository.getBearerTokensFlow().firstOrNull()
        return if (currentTokens != null) {
            logger?.v(TAG, "Bearer tokens are already present, skip fetching new tokens")
            currentTokens
        } else {
            val newTokens = authRepository.getNewUnauthorizedUserBearerTokens()
            authRepository.setBearerTokens(newTokens)
            logger?.v(TAG, "New Bearer tokens fetched: $newTokens")
            newTokens
        }
    }

    override suspend fun invoke(): Result<BearerTokens> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "FetchUnauthorizedUserBearerTokensUseCaseImpl"
    }
}
