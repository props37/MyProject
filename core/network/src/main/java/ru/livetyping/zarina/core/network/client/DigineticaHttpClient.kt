package ru.livetyping.zarina.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.analytics.HttpErrorLogger
import ru.livetyping.zarina.core.buildutil.BuildType

internal fun getDigineticaAutocompleteHttpClient(
    json: Json,
    buildType: BuildType,
    errorLogger: HttpErrorLogger,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json, buildType)
    install(DefaultRequest) {
        url(DIGINETICA_AUTOCOMPLETE_BASE_URL)
    }
    logErrors(errorLogger)
}

private const val DIGINETICA_AUTOCOMPLETE_BASE_URL = "https://autocomplete.diginetica.net/"
