package ru.livetyping.zarina.core.domain.model.common.exception

/**
 * Generic exception that wraps multiple validation exceptions. Note that trying to
 * pass [CombinedValidationException] as a cause is not supported and leads to throwing
 * [IllegalArgumentException].
 */
public class CombinedValidationException(
    public val causes: List<Exception>,
) : Exception() {
    init {
        causes.forEach { cause ->
            if (cause is CombinedValidationException) {
                throw IllegalArgumentException(
                    "Using CombinedValidationException as a cause is not supported",
                )
            }
            this.addSuppressed(cause)
        }
    }
}
