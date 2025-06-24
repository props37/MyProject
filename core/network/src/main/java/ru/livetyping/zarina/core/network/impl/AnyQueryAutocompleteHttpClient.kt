package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.analytics.HttpErrorLogger
import ru.livetyping.zarina.core.buildutil.BuildType

internal fun getAnyQueryAutocompleteHttpClient(
    json: Json,
    buildType: BuildType,
    errorLogger: HttpErrorLogger,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json, buildType)
    install(DefaultRequest) {
        url(BASE_URL)
    }
    logErrors(errorLogger)
}

private const val BASE_URL = "https://autocomplete.diginetica.net/"
