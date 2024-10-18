package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.usecase.auth.FetchUnauthorizedUserBearerTokensUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.auth.RefreshBearerTokensUseCase
import ru.livetyping.zarina.core.network.auth.BearerTokenService
import ru.livetyping.zarina.core.network.auth.BearerTokens
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class BearerTokenServiceModule {

    @Provides
    @Singleton
    fun provideBearerTokenService(
        getBearerTokensFlow: GetBearerTokensFlowUseCase,
        fetchUnauthorizedUserBearerTokens: FetchUnauthorizedUserBearerTokensUseCase,
        refreshBearerTokens: RefreshBearerTokensUseCase,
    ): BearerTokenService {
        return getBearerTokenService(
            getBearerTokensFlow = getBearerTokensFlow,
            fetchUnauthorizedUserBearerTokens = fetchUnauthorizedUserBearerTokens,
            refreshBearerTokens = refreshBearerTokens,
        )
    }

    private fun getBearerTokenService(
        getBearerTokensFlow: GetBearerTokensFlowUseCase,
        fetchUnauthorizedUserBearerTokens: FetchUnauthorizedUserBearerTokensUseCase,
        refreshBearerTokens: RefreshBearerTokensUseCase,
    ): BearerTokenService {
        return object : BearerTokenService {
            override suspend fun loadTokens(): BearerTokens? {
                var tokens = getBearerTokensFlow().firstOrNull()?.getOrNull()
                Timber.tag(BEARER_TOKEN_SERVICE_TAG).v("Current Bearer tokens loaded: $tokens")
                if (tokens == null) {
                    Timber
                        .tag(BEARER_TOKEN_SERVICE_TAG)
                        .v("Loaded Bearer tokens are null, trying to fetch new tokens")
                    tokens = fetchUnauthorizedUserBearerTokens().getOrNull()
                    Timber
                        .tag(BEARER_TOKEN_SERVICE_TAG)
                        .v("New Bearer tokens fetched: $tokens")
                }
                return tokens?.let { BearerTokens.from(it) }
            }

            override suspend fun refreshTokens(oldTokens: BearerTokens?): BearerTokens? {
                val params = RefreshBearerTokensUseCase.Params(oldTokens?.toBearerTokens())
                val newTokens = refreshBearerTokens(params).getOrNull()
                Timber.tag(BEARER_TOKEN_SERVICE_TAG).v("Bearer tokens refreshed: $newTokens")
                return newTokens?.let { BearerTokens.from(it) }
            }
        }
    }

    private companion object {
        private const val BEARER_TOKEN_SERVICE_TAG = "BearerTokenService"
    }
}
