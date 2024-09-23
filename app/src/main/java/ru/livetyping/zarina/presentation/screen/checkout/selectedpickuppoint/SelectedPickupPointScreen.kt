package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreenComponents.PickupPoint
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointViewModel.PickupPointState
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint.SelectedPickupPointViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun SelectedPickupPointScreen(
    navigate: (SelectedPickupPointScreenAction) -> Unit,
    viewModel: SelectedPickupPointViewModel = hiltViewModel(),
) {
    val pickupPointState by viewModel.pickupPointState.collectAsStateWithLifecycle()

    ScreenContent(
        pickupPointState = pickupPointState,
        onPickupPointDeliveryTypeClicked = viewModel::onPickupPointDeliveryTypeClicked,
        onPickupPointErrorRefreshClicked = viewModel::onPickupPointErrorRefreshClicked,
        onContinueClicked = viewModel::onContinueClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    pickupPointState: PickupPointState,
    onPickupPointDeliveryTypeClicked: (PickupPointDetails.DeliveryType) -> Unit,
    onPickupPointErrorRefreshClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SelectedPickupPointScreenAction) -> Unit,
) {
    SelectedPickupPointScreenBehavior(
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
        TopBar(onBackClicked = onBackClicked)

        PickupPoint(
            state = pickupPointState,
            onDeliveryTypeClicked = onPickupPointDeliveryTypeClicked,
            onContinueClicked = onContinueClicked,
            onErrorRefreshClicked = onPickupPointErrorRefreshClicked,
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
        // TODO: [Low] Add preview
    }
}
