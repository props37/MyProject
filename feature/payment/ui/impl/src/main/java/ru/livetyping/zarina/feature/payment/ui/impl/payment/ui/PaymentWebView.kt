package ru.livetyping.zarina.feature.payment.ui.impl.payment.ui

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.MailTo
import androidx.core.view.isVisible
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import timber.log.Timber
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun PaymentWebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    Box(modifier = modifier) {
        var isWebViewVisible by remember { mutableStateOf(false) }

        if (!isWebViewVisible) {
            ZarinaCircularLoader(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp),
            )
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    init(
                        isVisible = isWebViewVisible,
                        onPageLoaded = { isWebViewVisible = true },
                        context = currentContext,
                    )
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.isVisible = isWebViewVisible
            },
            onRelease = { webView ->
                webView.destroy()
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private fun WebView.init(
    isVisible: Boolean,
    onPageLoaded: () -> Unit,
    context: Context,
) {
    this.isVisible = isVisible

    settings.apply {
        javaScriptEnabled = true
        builtInZoomControls = false
        displayZoomControls = false
        setSupportZoom(false)
        loadsImagesAutomatically = true
        domStorageEnabled = true
    }
    scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
    isScrollbarFadingEnabled = true

    webViewClient = object : WebViewClientCompat() {
        override fun onPageFinished(view: WebView?, url: String?) {
            Timber.tag(TAG).v("onPageFinished: $url")
            onPageLoaded()
        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            val uri = request.url
            Timber.tag(TAG).v("shouldOverrideUrlLoading: $uri")
            return when {
                MailTo.isMailTo(uri) -> {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    tryStartActivity(intent, context)
                }

                uri.scheme?.startsWith(URI_SCHEME_TEL_PREFIX) == true -> {
                    val intent = Intent(Intent.ACTION_DIAL, uri)
                    tryStartActivity(intent, context)
                }

                !URLUtil.isNetworkUrl(uri.toString()) -> {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    tryStartActivity(
                        intent = intent,
                        context = context,
                        onActivityNotFound = {
                            val message = context.getString(RCommon.string.res_application_not_found)
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).also { it.show() }
                        },
                    )
                }

                else -> super.shouldOverrideUrlLoading(view, request)
            }
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceErrorCompat,
        ) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_RESOURCE_ERROR_GET_DESCRIPTION)) {
                Timber.tag(TAG).e("onReceivedError. Description: ${error.description}")
            }
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse,
        ) {
            Timber.tag(TAG).e("onReceivedHttpError. Reason: ${errorResponse.reasonPhrase}, status code: ${errorResponse.statusCode}")
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?,
        ) {
            Timber.tag(TAG).e("onReceivedSslError: $error")
        }
    }
}

private inline fun tryStartActivity(
    intent: Intent,
    context: Context,
    onActivityNotFound: (() -> Unit) = {},
): Boolean {
    return if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
        true
    } else {
        onActivityNotFound()
        false
    }
}

private const val URI_SCHEME_TEL_PREFIX = "tel"

private const val TAG = "PaymentWebView"
