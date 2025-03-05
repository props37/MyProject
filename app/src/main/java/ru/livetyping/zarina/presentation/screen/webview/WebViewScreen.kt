package ru.livetyping.zarina.presentation.screen.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun WebViewScreen(
    navigate: (WebViewScreenAction) -> Unit,
    viewModel: WebViewViewModel = hiltViewModel(),
) {
    val url by viewModel.url.collectAsStateWithLifecycle()
    val headers by viewModel.headers.collectAsStateWithLifecycle()

    ScreenContent(
        url = url,
        headers = headers,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    url: String,
    headers: Map<String, String>,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .safeDrawingPadding(),
    ) {
        ZarinaButton(
            onClick = onBackClicked,
            size = ZarinaButtonSize.Small,
            colors = ZarinaButtonDefaults.secondaryColors(),
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
            modifier = Modifier
                .zIndex(1f)
                .padding(top = 16.dp, start = 16.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .size(20.dp)
                    .rotate(270f),
            )
        }

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
