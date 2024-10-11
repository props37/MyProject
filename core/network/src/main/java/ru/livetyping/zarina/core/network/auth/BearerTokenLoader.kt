package ru.livetyping.zarina.core.network.auth

public interface BearerTokenLoader {
    public suspend fun load(): BearerTokens?
}
