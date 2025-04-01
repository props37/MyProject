package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointState

@Suppress("NAME_SHADOWING")
@Composable
internal fun SelectedPickupPoint(
    state: SelectedPickupPointState,
    onEvent: (SelectedPickupPointEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is SelectedPickupPointState.Success -> ContentKey.Success
                SelectedPickupPointState.Loading -> it
                is SelectedPickupPointState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is SelectedPickupPointState.Success -> TODO() // TODO: [Top] Implement
            SelectedPickupPointState.Loading -> TODO() // TODO: [Top] Implement
            is SelectedPickupPointState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = {
                        onEvent(SelectedPickupPointEvent.ErrorRefreshClicked)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(windowInsetsProvider())
                        .padding(16.dp),
                )
            }
        }
    }
}

private enum class ContentKey { Success }
