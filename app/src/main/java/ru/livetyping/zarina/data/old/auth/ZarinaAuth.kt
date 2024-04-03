package ru.livetyping.zarina.data.old.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeaders
import io.ktor.util.AttributeKey
import io.ktor.util.InternalAPI
import io.ktor.util.KtorDsl
import io.ktor.util.logging.KtorSimpleLogger

// This is a slightly modified version of https://github.com/ktorio/ktor/blob/main/ktor-client/ktor-client-plugins/ktor-client-auth/common/src/io/ktor/client/plugins/auth/Auth.kt
// Made because the original version doesn't support token expiry detection customization.
// TODO replace if it get fixed, follow this issue https://youtrack.jetbrains.com/issue/KTOR-5817

private val LOGGER = KtorSimpleLogger("ru.livetyping.zarina.data.old.auth.ZarinaAuth")

@KtorDsl
class ZarinaAuth private constructor(
    val providers: MutableList<AuthProvider> = mutableListOf(),
) {

    companion object Plugin : HttpClientPlugin<ZarinaAuth, ZarinaAuth> {
        /**
         * Shows that request should skip auth and refresh token procedure.
         */
        val AuthCircuitBreaker: AttributeKey<Unit> = AttributeKey("auth-request")

        override val key: AttributeKey<ZarinaAuth> = AttributeKey("DigestAuth")

        override fun prepare(block: ZarinaAuth.() -> Unit): ZarinaAuth {
            return ZarinaAuth().apply(block)
        }

        @OptIn(InternalAPI::class)
        override fun install(plugin: ZarinaAuth, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                plugin.providers.filter { it.sendWithoutRequest(context) }.forEach {
                    LOGGER.trace("Adding auth headers for ${context.url} from provider $it")
                    it.addRequestHeaders(context)
                }
            }

            scope.plugin(HttpSend).intercept { context ->
                val origin = execute(context)
                if (!origin.response.isTokenExpired()) return@intercept origin
                if (origin.request.attributes.contains(AuthCircuitBreaker)) return@intercept origin

                var call = origin

                val candidateProviders = HashSet(plugin.providers)

                while (call.response.isTokenExpired()) {
                    LOGGER.trace("Received 401 for ${call.request.url}")
                    val headerValues = call.response.headers.getAll(HttpHeaders.WWWAuthenticate)
                    val authHeaders = headerValues?.map { parseAuthorizationHeaders(it) }?.flatten()
                        ?: emptyList()

                    var providerOrNull: AuthProvider? = null
                    var authHeader: HttpAuthHeader? = null

                    when {
                        authHeaders.isEmpty() && candidateProviders.size == 1 -> {
                            providerOrNull = candidateProviders.first()
                        }

                        authHeaders.isEmpty() -> {
                            LOGGER.trace(
                                "401 response ${call.request.url} has no or empty \"WWW-Authenticate\" header. " +
                                        "Can not add or refresh token"
                            )
                            return@intercept call
                        }

                        else -> authHeader = authHeaders.find { header ->
                            providerOrNull = candidateProviders.find { it.isApplicable(header) }
                            providerOrNull != null
                        }
                    }
                    val provider = providerOrNull ?: run {
                        LOGGER.trace("Can not provider find auth provider for ${call.request.url}")
                        return@intercept call
                    }
                    LOGGER.trace("Using provider $provider for ${call.request.url}")

                    LOGGER.trace("Refreshing token for ${call.request.url}")
                    if (!provider.refreshToken(call.response)) {
                        LOGGER.trace("Refreshing token failed for ${call.request.url}")
                        return@intercept call
                    }

                    candidateProviders.remove(provider)

                    val request = HttpRequestBuilder()
                    request.takeFromWithExecutionContext(context)
                    provider.addRequestHeaders(request, authHeader)
                    request.attributes.put(AuthCircuitBreaker, Unit)

                    LOGGER.trace("Sending new request to ${call.request.url}")
                    call = execute(request)
                }
                return@intercept call
            }
        }

        fun HttpResponse.isTokenExpired(): Boolean {
            return this.status == HttpStatusCode.Unauthorized || this.status == HttpStatusCode.Forbidden
        }

    }
}

@KtorDsl
class BearerAuthConfig {
    internal var _refreshTokens: suspend RefreshTokensParams.() -> BearerTokens? = { null }
    internal var _loadTokens: suspend () -> BearerTokens? = { null }
    internal var _sendWithoutRequest: (HttpRequestBuilder) -> Boolean = { true }

    var realm: String? = null

    /**
     * Configures a callback that refreshes a token when the 401 status code is received.
     */
    fun refreshTokens(block: suspend RefreshTokensParams.() -> BearerTokens?) {
        _refreshTokens = block
    }

    /**
     * Configures a callback that loads a cached token from a local storage.
     * Note: Using the same client instance here to make a request will result in a deadlock.
     */
    fun loadTokens(block: suspend () -> BearerTokens?) {
        _loadTokens = block
    }

    /**
     * Sends credentials without waiting for [HttpStatusCode.Unauthorized].
     */
    fun sendWithoutRequest(block: (HttpRequestBuilder) -> Boolean) {
        _sendWithoutRequest = block
    }
}

fun ZarinaAuth.bearer(block: BearerAuthConfig.() -> Unit) {
    with(BearerAuthConfig().apply(block)) {
        this@bearer.providers.add(
            BearerAuthProvider(
                _refreshTokens,
                _loadTokens,
                _sendWithoutRequest,
                realm
            )
        )
    }
}
