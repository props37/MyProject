package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
internal fun ProductAvailabilityInStoresScreen(
    navigate: (ProductAvailabilityInStoresScreenAction) -> Unit,
    viewModel: ProductAvailabilityInStoresViewModel = hiltViewModel(),
) {
    ScreenContent(
        navigate = navigate,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    navigate: (ProductAvailabilityInStoresScreenAction) -> Unit,
    sideEffects: Flow<ProductAvailabilityInStoresViewModel.SideEffect>,
) {
    ProductAvailabilityInStoresScreenBehavior(
        navigate = navigate,
        sideEffects = sideEffects,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {

    }
}
