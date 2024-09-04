package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryComponents.FilterBlock
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryComponents.ViewModeHorizontalPager
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryComponents.ViewModeTabRow
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.PickupPointsState
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun CheckoutPickupPointDeliveryScreen(
    navigate: (CheckoutPickupPointDeliveryScreenAction) -> Unit,
    viewModel: CheckoutPickupPointDeliveryViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val viewModes by viewModel.viewModes.collectAsStateWithLifecycle()
    val currentViewMode by viewModel.currentViewMode.collectAsStateWithLifecycle()
    val pickupPointsState by viewModel.pickupPointsState.collectAsStateWithLifecycle()

    ScreenContent(
        step = step,
        stepCount = stepCount,
        viewModes = viewModes,
        currentViewMode = currentViewMode,
        onViewModeChanged = viewModel::onViewModeChanged,
        nameOrAddressTextFieldState = viewModel.nameOrAddressFilterTextFieldState,
        pickupPointsState = pickupPointsState,
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
    viewModes: List<ViewMode>,
    currentViewMode: ViewMode,
    onViewModeChanged: (ViewMode) -> Unit,
    nameOrAddressTextFieldState: TextFieldState,
    pickupPointsState: PickupPointsState,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutPickupPointDeliveryScreenAction) -> Unit,
) {
    CheckoutPickupPointDeliveryScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            ),
    ) {
        CheckoutComponents.TopBar(
            title = stringResource(R.string.pickup_point_delivery),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
        )

        FilterBlock(
            nameOrAddressFilterTextFieldState = nameOrAddressTextFieldState,
            modifier = Modifier.padding(vertical = 4.dp),
        )

        val viewModePagerState = rememberPagerState { viewModes.size }
        PagerTabRowIntegration(
            pagerState = viewModePagerState,
            tabs = viewModes,
            currentTab = currentViewMode,
            onCurrentTabChanged = onViewModeChanged,
        )

        ViewModeTabRow(
            modes = viewModes,
            currentMode = currentViewMode,
            onModeChanged = onViewModeChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        ViewModeHorizontalPager(
            pagerState = viewModePagerState,
            viewModes = viewModes,
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
