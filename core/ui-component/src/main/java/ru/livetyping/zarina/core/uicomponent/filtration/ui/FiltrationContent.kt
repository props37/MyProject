package ru.livetyping.zarina.core.uicomponent.filtration.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationState
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationTopBarState
import ru.livetyping.zarina.core.uikit.overlay.ZarinaRefreshingOverlay

@Composable
public fun FiltrationContent(
    topBarState: FiltrationTopBarState,
    onTopBarEvent: (FiltrationTopBarEvent) -> Unit,
    filtrationState: FiltrationState,
    onFiltrationEvent: (FiltrationEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    // Do not use passed modifier here since refreshing overlay should take the whole available space
    Box(modifier = Modifier) {
        Column(modifier = modifier.windowInsetsPadding(windowInsetsProvider())) {
            FiltrationTopBar(
                state = topBarState,
                onEvent = onTopBarEvent,
            )

            Filters(
                state = filtrationState,
                onEvent = onFiltrationEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val isRefreshing = (filtrationState as? FiltrationState.Success)?.isRefreshing == true
        AnimatedVisibility(
            visible = isRefreshing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize(),
        ) {
            ZarinaRefreshingOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}
