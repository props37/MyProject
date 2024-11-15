package ru.livetyping.zarina.core.network.auth

import ru.livetyping.zarina.core.domain.model.common.Token
import io.ktor.client.plugins.auth.providers.BearerTokens as KtorBearerTokens
import ru.livetyping.zarina.core.domain.model.auth.BearerTokens as DomainBearerTokens

public data class BearerTokens(
    public val accessToken: String,
    public val refreshToken: String,
) {
    internal fun toKtorBearerTokens(): KtorBearerTokens {
        return KtorBearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    public fun toBearerTokens(): DomainBearerTokens {
        return DomainBearerTokens(
            accessToken = Token(accessToken),
            refreshToken = Token(refreshToken),
        )
    }

    public companion object {
        internal fun from(tokens: KtorBearerTokens): BearerTokens {
            val refreshToken = checkNotNull(tokens.refreshToken) { "Refresh token is null" }
            return BearerTokens(
                accessToken = tokens.accessToken,
                refreshToken = refreshToken,
            )
        }

        public fun from(tokens: DomainBearerTokens): BearerTokens {
            return BearerTokens(
                accessToken = tokens.accessToken.value,
                refreshToken = tokens.refreshToken.value,
            )
        }
    }
}
