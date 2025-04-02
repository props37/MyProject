package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
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
            is SelectedPickupPointState.Success -> {
                SelectedPickupPointSuccess(
                    state = state,
                    onDeliveryTypeClicked = {
                        onEvent(SelectedPickupPointEvent.DeliveryTypeClicked(it))
                    },
                    onContinueClicked = { onEvent(SelectedPickupPointEvent.ContinueClicked) },
                    windowInsetsProvider = windowInsetsProvider,
                )
            }

            SelectedPickupPointState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(windowInsetsProvider()),
                ) {
                    ZarinaCircularLoader(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center),
                    )
                }
            }

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
