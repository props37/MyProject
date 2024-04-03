package ru.livetyping.zarina.domain.common.exception

open class ValidationException(message: String = "Validation failed") : Exception(message) {
    companion object {
        /**
         * Returns [ValidationException] with suppressed [exceptions] if they are present or
         * `null` otherwise.
         */
        fun from(vararg exceptions: Throwable?): ValidationException? = from(exceptions.toList())

        /**
         * Returns [ValidationException] with suppressed [exceptions] if they are present or
         * `null` otherwise.
         */
        fun from(exceptions: List<Throwable?>): ValidationException? {
            val exception = ValidationException()
            for (e in exceptions) {
                if (e != null) exception.addSuppressed(e)
            }
            return if (exception.suppressedExceptions.isNotEmpty()) exception else null
        }
    }
}
