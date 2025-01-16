package ru.livetyping.zarina.presentation.screen.payment

import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.screen.payment.PaymentScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.payment.PaymentViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

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
    PaymentScreenBehavior(
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
                        isVisible = isWebViewVisible
                        loadUrl(paymentUrl.value)
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                if (!isWebViewVisible && url == paymentUrl.value) {
                                    isWebViewVisible = true
                                }
                            }
                        }
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
