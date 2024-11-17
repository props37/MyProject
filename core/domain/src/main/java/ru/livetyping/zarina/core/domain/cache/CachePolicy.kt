package ru.livetyping.zarina.core.domain.cache

public sealed class CachePolicy {
    public data object LocalOnly : CachePolicy()

    public data class LocalFirstThenRemote(
        val expirationPolicy: CacheExpirationPolicy,
        val updatePolicy: CacheUpdatePolicy,
    ) : CachePolicy()

    public data class Remote(
        val updatePolicy: CacheUpdatePolicy,
    ) : CachePolicy()
}
