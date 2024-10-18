package ru.livetyping.zarina.core.domain.usecase.auth

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public class RefreshBearerTokensUseCase(
    private val authRepository: AuthRepository,
    private val logger: UseCaseLogger?,
) : UseCase<Params, BearerTokens?>(logger) {

    override suspend fun execute(params: Params): BearerTokens {
        val oldTokens = params.oldTokens
        return if (oldTokens != null) {
            refreshTokens(oldTokens)
        } else {
            val newUnauthorizedUserTokens = authRepository.getNewUnauthorizedUserBearerTokens()
            authRepository.setBearerTokens(newUnauthorizedUserTokens)
            newUnauthorizedUserTokens
        }
    }

    private suspend fun refreshTokens(oldTokens: BearerTokens): BearerTokens {
        return try {
            val newTokens = authRepository.refreshBearerTokens(oldTokens)
            authRepository.setBearerTokens(newTokens)
            logger?.v(TAG, "Bearer tokens refreshed")
            newTokens
        } catch (e: Exception) {
            logger?.e(TAG, e, "Failed to refresh Bearer tokens, request force signout")
            // TODO: [Top] Implement
            throw e
        }
    }

    public data class Params(val oldTokens: BearerTokens?)

    private companion object {
        private const val TAG = "RefreshBearerTokensUseCase"
    }
}
