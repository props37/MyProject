package ru.livetyping.zarina.core.network.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.request
import ru.livetyping.zarina.core.analytics.HttpError
import ru.livetyping.zarina.core.analytics.HttpErrorLogger
import kotlin.coroutines.cancellation.CancellationException

internal fun HttpClientConfig<*>.logErrors(logger: HttpErrorLogger) {
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            val error = when (cause) {
                is ResponseException -> {
                    HttpError {
                        val response = cause.response
                        request = buildString {
                            append(response.request.method.value)
                            append(SPACE)
                            append(response.request.url.toString())
                        }
                        statusCode = response.status.value
                    }
                }

                is CancellationException -> null
                else -> HttpError { description = cause.message }
            }

            if (error != null) logger.logHttpError(error)
        }
    }
}

private const val SPACE = ' '
