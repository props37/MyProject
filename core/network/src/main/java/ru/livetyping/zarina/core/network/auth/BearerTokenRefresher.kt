package ru.livetyping.zarina.core.network.auth

public interface BearerTokenRefresher {
    public suspend fun refresh(oldTokens: BearerTokens?): BearerTokens?
}
