package ru.livetyping.zarina.presentation.screen.payment

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.screen.payment.PaymentScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.payment.PaymentViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import timber.log.Timber

@Composable
fun PaymentScreen(
    navigate: (PaymentScreenAction) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel(),
) {
    val paymentUrl by viewModel.paymentUrl.collectAsStateWithLifecycle()

    ScreenContent(
        paymentUrl = paymentUrl,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    paymentUrl: Url,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (PaymentScreenAction) -> Unit,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    PaymentScreenBehavior(
        onBackClicked = onBackClicked,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .safeDrawingPadding(),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Box {
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
                        loadUrl(paymentUrl.value)
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
            Timber.tag(TAG_WEB_VIEW).v("onPageFinished: $url")
            onPageLoaded()
        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            val uri = request.url
            Timber.tag(TAG_WEB_VIEW).v("shouldOverrideUrlLoading: $uri")
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
                            val message = context.getString(R.string.application_not_found)
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
            error: WebResourceErrorCompat
        ) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_RESOURCE_ERROR_GET_DESCRIPTION)) {
                Timber.tag(TAG_WEB_VIEW).e("onReceivedError. Description: ${error.description}")
            }
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            Timber.tag(TAG_WEB_VIEW).e("onReceivedHttpError. Reason: ${errorResponse.reasonPhrase}, status code: ${errorResponse.statusCode}")
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Timber.tag(TAG_WEB_VIEW).e("onReceivedSslError: $error")
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

private const val TAG_WEB_VIEW = "PaymentWebView"
