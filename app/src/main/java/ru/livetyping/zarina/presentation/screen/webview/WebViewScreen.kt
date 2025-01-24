package ru.livetyping.zarina.presentation.screen.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun WebViewScreen(
    navigate: (WebViewScreenAction) -> Unit,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val url by viewModel.url.collectAsStateWithLifecycle()

    ScreenContent(
        url = url,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    url: String,
    onBackClicked: () -> Unit,
    sideEffects: Flow<WebViewViewModel.SideEffect>,
    navigate: (WebViewScreenAction) -> Unit,
) {
    WebViewScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    var webView by remember { mutableStateOf<WebView?>(null) }
    var webViewCanGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = webViewCanGoBack) {
        webView?.goBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .safeDrawingPadding(),
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
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

                    loadUrl(url)
                }.also { webView = it }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url)
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
