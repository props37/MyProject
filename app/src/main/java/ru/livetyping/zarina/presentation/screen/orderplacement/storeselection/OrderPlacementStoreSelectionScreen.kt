package ru.livetyping.zarina.presentation.screen.orderplacement.storeselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.orderplacement.common.OrderPlacementComponents
import ru.livetyping.zarina.presentation.screen.orderplacement.storeselection.OrderPlacementStoreSelectionViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun OrderPlacementStoreSelectionScreen(
    navigate: (OrderPlacementStoreSelectionScreenAction) -> Unit,
    viewModel: OrderPlacementStoreSelectionViewModel = hiltViewModel(),
) {
    ScreenContent(
        step = 2, // TODO: [High] Implement
        stepCount = 3, // TODO: [High] Implement
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
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (OrderPlacementStoreSelectionScreenAction) -> Unit,
) {
    OrderPlacementStoreSelectionScreenBehavior(
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
        OrderPlacementComponents.TopBar(
            title = stringResource(R.string.store_selection),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
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
