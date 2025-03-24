package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

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
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun PickupStoreSelectorScreen(
    navActions: PickupStoreSelectorNavActions,
    viewModel: PickupStoreSelectorViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<PickupStoreSelectorSideEffect>,
    navActions: PickupStoreSelectorNavActions,
) {
    PickupStoreSelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
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
