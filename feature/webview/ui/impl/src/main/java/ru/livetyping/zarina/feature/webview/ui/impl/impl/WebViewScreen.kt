package ru.livetyping.zarina.feature.webview.ui.impl.impl

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature
import ru.livetyping.zarina.feature.webview.ui.impl.impl.component.FloatingBackButton

@Composable
internal fun WebViewScreen(
    navActions: WebViewFeature.NavActions,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val url by viewModel.url.collectAsStateWithLifecycle()
    val headers by viewModel.headers.collectAsStateWithLifecycle()

    ScreenContent(
        url = url,
        headers = headers,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    url: String,
    headers: Map<String, String>,
    onBackClicked: () -> Unit,
    sideEffects: Flow<WebViewSideEffect>,
    navActions: WebViewFeature.NavActions,
) {
    WebViewScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    var webView by remember { mutableStateOf<WebView?>(null) }
    var webViewCanGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = webViewCanGoBack) {
        webView?.goBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .safeDrawingPadding(),
    ) {
        FloatingBackButton(
            onClick = onBackClicked,
            modifier = Modifier
                .zIndex(1f)
                .padding(top = 16.dp, start = 16.dp),
        )

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    webViewClient = object : WebViewClient() {
                        override fun doUpdateVisitedHistory(
                            webView: WebView?,
                            url: String?,
                            isReload: Boolean,
                        ) {
                            webViewCanGoBack = webView?.canGoBack() == true
                        }
                    }

                    loadUrl(url, headers)
                }.also { webView = it }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url, headers)
                }
            },
            onRelease = { _webView ->
                webView = null
                _webView.destroy()
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
