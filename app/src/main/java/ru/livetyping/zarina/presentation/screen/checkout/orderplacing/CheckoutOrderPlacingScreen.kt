package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutOrderPlacingScreen(
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
    viewModel: CheckoutOrderPlacingViewModel = hiltViewModel(),
) {
    val state by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()

    ScreenContent(
        step = state,
        stepCount = stepCount,
        onBackClicked = viewModel::onBackClicked,
        onClosesClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    step: Int,
    stepCount: Int,
    onBackClicked: () -> Unit,
    onClosesClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
) {
    CheckoutOrderPlacingScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout),
                )
                .imePadding()
                .bottomNavBarPadding(WindowInsets.ime),
        ) {
            CheckoutComponents.TopBar(
                title = stringResource(R.string.order_confirmation),
                step = step,
                stepCount = stepCount,
                isBackButtonVisible = true,
                onBackClicked = onBackClicked,
                onCloseClicked = onClosesClicked,
            )
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
