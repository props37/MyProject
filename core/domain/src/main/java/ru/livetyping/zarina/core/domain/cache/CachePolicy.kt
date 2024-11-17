package ru.livetyping.zarina.core.domain.cache

public sealed class CachePolicy {
    public data object LocalOnly : CachePolicy()

    public data class LocalFirstThenRemote(
        val expirationPolicy: CacheExpirationPolicy = CacheExpirationPolicy.UNLIMITED,
        val updatePolicy: CacheUpdatePolicy = CacheUpdatePolicy.UPDATE,
    ) : CachePolicy()

    public data class RemoteOnly(
        val updatePolicy: CacheUpdatePolicy = CacheUpdatePolicy.UPDATE,
    ) : CachePolicy()
}
