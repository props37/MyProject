package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.BuildType

internal fun getMindboxHttpClient(
    json: Json,
    buildType: BuildType,
    mindboxKey: String,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json, buildType)
    install(DefaultRequest) {
        url(BASE_URL)
        header(AUTHORIZATION_KEY, getAuthorizationValue(mindboxKey))
    }
}

private fun getAuthorizationValue(mindboxKey: String): String {
    return "$AUTHORIZATION_VALUE_PREFIX $mindboxKey"
}

private const val BASE_URL = "https://api.mindbox.ru/"
private const val AUTHORIZATION_KEY = "Authorization"
private const val AUTHORIZATION_VALUE_PREFIX = "SecretKey"
