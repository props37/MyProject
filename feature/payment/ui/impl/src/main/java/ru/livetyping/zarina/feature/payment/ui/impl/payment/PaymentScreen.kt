package ru.livetyping.zarina.feature.payment.ui.impl.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature
import ru.livetyping.zarina.feature.payment.ui.impl.payment.ui.PaymentWebView
import ru.livetyping.zarina.feature.payment.ui.impl.payment.ui.TopBar

@Composable
internal fun PaymentScreen(
    navActions: PaymentFeature.NavActions,
    viewModel: PaymentViewModel = hiltViewModel(),
) {
    val paymentUrl by viewModel.paymentUrl.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onBackClicked)

    ScreenContent(
        paymentUrl = paymentUrl,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    paymentUrl: String,
    onBackClicked: () -> Unit,
    sideEffects: Flow<PaymentSideEffect>,
    navActions: PaymentFeature.NavActions,
) {
    PaymentScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .safeDrawingPadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(onBackClicked = onBackClicked)

        PaymentWebView(paymentUrl)
    }
}
