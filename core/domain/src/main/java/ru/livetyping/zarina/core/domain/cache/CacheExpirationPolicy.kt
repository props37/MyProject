package ru.livetyping.zarina.core.domain.cache

import kotlin.time.Duration

public data class CacheExpirationPolicy(
    val maxLifespan: Duration,
) {
    public companion object {
        public val UNLIMITED: CacheExpirationPolicy
            get() = CacheExpirationPolicy(Duration.INFINITE)
    }
}
