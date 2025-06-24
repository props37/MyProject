package ru.livetyping.zarina.core.analytics

public class HttpError private constructor(
    public val request: String?,
    public val statusCode: Int?,
    public val description: String?,
) {
    public class Builder {
        public var request: String? = null
        public var statusCode: Int? = null
        public var description: String? = null

        public fun build(): HttpError {
            return HttpError(request, statusCode, description)
        }
    }
}

public inline fun HttpError(
    builderAction: HttpError.Builder.() -> Unit,
): HttpError {
    val builder = HttpError.Builder()
    builder.builderAction()
    return builder.build()
}
