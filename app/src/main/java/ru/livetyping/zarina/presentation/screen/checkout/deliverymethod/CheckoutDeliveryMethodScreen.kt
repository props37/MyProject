package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
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
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodScreenComponents.DeliveryMethods
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutDeliveryMethodScreen(
    navigate: (CheckoutDeliveryMethodScreenAction) -> Unit,
    viewModel: CheckoutDeliveryMethodViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenContent(
        step = step,
        stepCount = stepCount,
        state = state,
        onDeliveryMethodClicked = viewModel::onDeliveryMethodClicked,
        onDeliveryMethodsErrorRefreshClicked = viewModel::onDeliveryMethodsErrorRefreshClicked,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    step: Int,
    stepCount: Int,
    state: State,
    onDeliveryMethodClicked: (DeliveryMethod) -> Unit,
    onDeliveryMethodsErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutDeliveryMethodScreenAction) -> Unit,
) {
    CheckoutDeliveryMethodScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        CheckoutComponents.TopBar(
            title = stringResource(R.string.delivery_method),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
        )

        DeliveryMethods(
            state = state,
            onMethodClicked = onDeliveryMethodClicked,
            onErrorRefreshClicked = onDeliveryMethodsErrorRefreshClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // Add preview
    }
}
