package ru.livetyping.zarina.ui.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.order.OrderScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.order.OrderViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun OrderScreen(
    navigate: (OrderScreenAction) -> Unit,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (OrderScreenAction) -> Unit,
) {
    OrderScreenBehavior(
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
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(
            orderNumber = remember { FakeDataGenerator.getOrderItem().number }, // TODO: [High] Implement
            onBackClicked = onBackClicked,
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
