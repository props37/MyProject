package ru.livetyping.zarina.presentation.screen.payment

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.common.Url
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

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    loadUrl(paymentUrl.value)
                    settings.apply {
                        javaScriptEnabled = true
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
