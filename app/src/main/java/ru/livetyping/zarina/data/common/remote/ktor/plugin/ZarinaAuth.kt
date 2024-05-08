package ru.livetyping.zarina.data.common.remote.ktor.plugin

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.Sender
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
import io.ktor.util.collections.ConcurrentMap
import io.ktor.util.logging.KtorSimpleLogger
import java.util.concurrent.atomic.AtomicInteger

// This is a slightly modified version of https://github.com/ktorio/ktor/blob/main/ktor-client/ktor-client-plugins/ktor-client-auth/common/src/io/ktor/client/plugins/auth/Auth.kt
// Made because the original version doesn't support token expiry detection customization.
// TODO: [Medium] Replace if it get fixed, follow the issue https://youtrack.jetbrains.com/issue/KTOR-5817

private val LOGGER = KtorSimpleLogger("ru.livetyping.zarina.data.common.remote.ktor.plugin.ZarinaAuth")

private class AtomicCounter {
    val atomic = AtomicInteger(0)
}

/**
 * A client's plugin that handles authentication and authorization.
 * Typical usage scenarios include logging in users and gaining access to specific resources.
 *
 * You can learn more from [Authentication and authorization](https://ktor.io/docs/auth.html).
 *
 * [providers] - list of auth providers to use.
 */
@KtorDsl
class ZarinaAuth private constructor(
    val providers: MutableList<AuthProvider> = mutableListOf(),
) {

    companion object Plugin : HttpClientPlugin<ZarinaAuth, ZarinaAuth> {
        /**
         * Shows that request should skip auth and refresh token procedure.
         */
        val AuthCircuitBreaker: AttributeKey<Unit> = AttributeKey("auth-request")

        override val key: AttributeKey<ZarinaAuth> = AttributeKey("DigestZarinaAuth")

        override fun prepare(block: ZarinaAuth.() -> Unit): ZarinaAuth {
            return ZarinaAuth().apply(block)
        }

        private val tokenVersions = ConcurrentMap<AuthProvider, AtomicCounter>()
        private val tokenVersionsAttributeKey =
            AttributeKey<MutableMap<AuthProvider, Int>>("ProviderVersionAttributeKey")

        override fun install(plugin: ZarinaAuth, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                plugin.providers.filter { it.sendWithoutRequest(context) }.forEach { provider ->
                    LOGGER.trace("Adding auth headers for ${context.url} from provider $provider")
                    val tokenVersion = tokenVersions.computeIfAbsent(provider) { AtomicCounter() }
                    val requestTokenVersions = context.attributes
                        .computeIfAbsent(tokenVersionsAttributeKey) { mutableMapOf() }
                    requestTokenVersions[provider] = tokenVersion.atomic.get()
                    provider.addRequestHeaders(context)
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

                    val (provider, authHeader) = findProvider(call, candidateProviders) ?: run {
                        LOGGER.trace("Can not find auth provider for ${call.request.url}")
                        return@intercept call
                    }

                    LOGGER.trace("Using provider $provider for ${call.request.url}")

                    candidateProviders.remove(provider)
                    if (!refreshTokenIfNeeded(call, provider, context)) return@intercept call
                    call = executeWithNewToken(call, provider, context, authHeader)
                }
                return@intercept call
            }
        }

        @OptIn(InternalAPI::class)
        private fun findProvider(
            call: HttpClientCall,
            candidateProviders: Set<AuthProvider>,
        ): Pair<AuthProvider, HttpAuthHeader?>? {
            val headerValues = call.response.headers.getAll(HttpHeaders.WWWAuthenticate)
            val authHeaders =
                headerValues?.map { parseAuthorizationHeaders(it) }?.flatten() ?: emptyList()

            return when {
                authHeaders.isEmpty() && candidateProviders.size == 1 -> {
                    candidateProviders.first() to null
                }

                authHeaders.isEmpty() -> {
                    LOGGER.trace(
                        "401 response ${call.request.url} has no or empty \"WWW-Authenticate\" header. " +
                                "Can not add or refresh token"
                    )
                    null
                }

                else -> authHeaders.firstNotNullOfOrNull { header ->
                    candidateProviders.find { it.isApplicable(header) }?.let { it to header }
                }
            }
        }

        private suspend fun refreshTokenIfNeeded(
            call: HttpClientCall,
            provider: AuthProvider,
            request: HttpRequestBuilder,
        ): Boolean {
            val tokenVersion = tokenVersions.computeIfAbsent(provider) { AtomicCounter() }
            val requestTokenVersions = request.attributes
                .computeIfAbsent(tokenVersionsAttributeKey) { mutableMapOf() }
            val requestTokenVersion = requestTokenVersions[provider]

            if (requestTokenVersion != null && requestTokenVersion >= tokenVersion.atomic.get()) {
                LOGGER.trace("Refreshing token for ${call.request.url}")
                if (!provider.refreshToken(call.response)) {
                    LOGGER.trace("Refreshing token failed for ${call.request.url}")
                    return false
                } else {
                    requestTokenVersions[provider] = tokenVersion.atomic.incrementAndGet()
                }
            }
            return true
        }

        @OptIn(InternalAPI::class)
        private suspend fun Sender.executeWithNewToken(
            call: HttpClientCall,
            provider: AuthProvider,
            oldRequest: HttpRequestBuilder,
            authHeader: HttpAuthHeader?,
        ): HttpClientCall {
            val request = HttpRequestBuilder()
            request.takeFromWithExecutionContext(oldRequest)
            provider.addRequestHeaders(request, authHeader)
            request.attributes.put(AuthCircuitBreaker, Unit)

            LOGGER.trace("Sending new request to ${call.request.url}")
            return execute(request)
        }

        private fun HttpResponse.isTokenExpired(): Boolean {
            return status == HttpStatusCode.Unauthorized || status == HttpStatusCode.Forbidden
        }
    }
}

fun ZarinaAuth.bearer(block: BearerAuthConfig.() -> Unit) {
    with(BearerAuthConfig().apply(block)) {
        this@bearer.providers.add(
            BearerAuthProvider(
                _refreshTokens,
                _loadTokens,
                _sendWithoutRequest,
                realm,
            )
        )
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
