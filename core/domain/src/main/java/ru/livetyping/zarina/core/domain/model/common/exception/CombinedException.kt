package ru.livetyping.zarina.core.domain.model.common.exception

/**
 * Generic exception that wraps multiple exceptions. Note that trying to
 * pass [CombinedException] as a cause should be avoided and leads to throwing
 * [IllegalArgumentException].
 */
public class CombinedException(
    public val causes: List<Exception>,
) : Exception() {
    init {
        causes.forEach { cause ->
            if (cause is CombinedException) {
                throw IllegalArgumentException(
                    "Using CombinedValidationException as a cause is not supported",
                )
            }
            this.addSuppressed(cause)
        }
    }
}
