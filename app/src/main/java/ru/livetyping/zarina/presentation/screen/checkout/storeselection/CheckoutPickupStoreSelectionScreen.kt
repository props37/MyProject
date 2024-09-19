package ru.livetyping.zarina.presentation.screen.checkout.storeselection

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
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutPickupStoreSelectionScreenComponents.City
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutPickupStoreSelectionScreenComponents.Stores
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutPickupStoreSelectionViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutPickupStoreSelectionViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutPickupStoreSelectionScreen(
    navigate: (CheckoutPickupStoreSelectionScreenAction) -> Unit,
    viewModel: CheckoutPickupStoreSelectionViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenContent(
        step = step,
        stepCount = stepCount,
        city = city,
        state = state,
        onStoreClicked = viewModel::onStoreClicked,
        onStoresErrorRefreshClicked = viewModel::onStoresErrorRefreshClicked,
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
    city: City?,
    state: State,
    onStoreClicked: (PickupStore) -> Unit,
    onStoresErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutPickupStoreSelectionScreenAction) -> Unit,
) {
    CheckoutPickupStoreSelectionScreenBehavior(
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
            title = stringResource(R.string.store_selection),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
        )

        City(city = city)

        Stores(
            state = state,
            onStoreClicked = onStoreClicked,
            onStoresErrorRefreshClicked = onStoresErrorRefreshClicked,
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
