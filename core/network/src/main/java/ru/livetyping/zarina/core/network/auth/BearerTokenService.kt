package ru.livetyping.zarina.core.network.auth

public interface BearerTokenService {
    public suspend fun loadTokens(): BearerTokens?

    public suspend fun refreshTokens(oldTokens: BearerTokens?): BearerTokens?
}
