package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.component.SizeRow
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.component.TopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.AvailabilityInStoresEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.AvailabilityInStoresState
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.SizeState

@Composable
internal fun AvailabilityInStoresScreen(
    navActions: AvailabilityInStoresNavActions,
    viewModel: AvailabilityInStoresViewModel = hiltViewModel(),
) {
    val availabilityInStoresState by viewModel.availabilityInStoresState.collectAsStateWithLifecycle()

    ScreenContent(
        availabilityInStoresState = availabilityInStoresState,
        onAvailabilityInStoresEvent = viewModel::onAvailabilityInStoresEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    availabilityInStoresState: AvailabilityInStoresState,
    onAvailabilityInStoresEvent: (AvailabilityInStoresEvent) -> Unit,
    sideEffects: Flow<AvailabilityInStoresSideEffect>,
    navActions: AvailabilityInStoresNavActions,
) {
    AvailabilityInStoresScreenBehavior(
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
        TopBar(
            onBackClicked = {
                onAvailabilityInStoresEvent(AvailabilityInStoresEvent.BackClicked)
            },
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (availabilityInStoresState.sizeState is SizeState.Success) {
            SizeRow(
                state = availabilityInStoresState.sizeState,
                onSizeClicked = { size ->
                    onAvailabilityInStoresEvent(AvailabilityInStoresEvent.SizeClicked(size))
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // TODO: [Top] Implement
    }
}
