package ru.livetyping.zarina.core.network.auth

import io.ktor.client.plugins.auth.providers.BearerTokens as KtorBearerTokens

public data class BearerTokens(
    public val accessToken: String,
    public val refreshToken: String,
) {
    internal fun toBearerTokens(): KtorBearerTokens {
        return KtorBearerTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    internal companion object {
        fun from(tokens: KtorBearerTokens): BearerTokens {
            return BearerTokens(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
            )
        }
    }
}
