package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.model.GiftCertificateState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.ui.GiftCertificateInput
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.ui.TopBar

@Composable
internal fun GiftCertificateScreen(
    navActions: GiftCertificateNavActions,
    viewModel: GiftCertificateViewModel = hiltViewModel(),
) {
    val giftCertificateState by viewModel.giftCertificateState.collectAsStateWithLifecycle()

    ScreenContent(
        giftCertificateState = giftCertificateState,
        onCloseClicked = viewModel::onCloseClicked,
        onApplyClicked = viewModel::onApplyClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    giftCertificateState: GiftCertificateState,
    onCloseClicked: () -> Unit,
    onApplyClicked: () -> Unit,
    sideEffects: Flow<GiftCertificateSideEffect>,
    navActions: GiftCertificateNavActions,
) {
    GiftCertificateScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(onCloseClicked = onCloseClicked)

        GiftCertificateInput(
            state = giftCertificateState,
            onApplyClicked = onApplyClicked,
            windowInsetsProvider = { WindowInsets.safeDrawing },
        )
    }
}
