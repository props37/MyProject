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
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationState
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarState
import ru.livetyping.zarina.core.uikit.overlay.ZarinaRefreshingOverlay

@Composable
public fun ProductFiltrationContent(
    topBarState: ProductFiltrationTopBarState,
    onTopBarEvent: (ProductFiltrationTopBarEvent) -> Unit,
    filtrationState: ProductFiltrationState,
    onFiltrationEvent: (ProductFiltrationEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    // Do not use passed modifier here since refreshing overlay should take the whole available space
    Box(modifier = Modifier) {
        Column(modifier = modifier.windowInsetsPadding(windowInsetsProvider())) {
            ProductFiltrationTopBar(
                state = topBarState,
                onEvent = onTopBarEvent,
            )

            ProductFilters(
                state = filtrationState,
                onEvent = onFiltrationEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val isRefreshing =
            (filtrationState as? ProductFiltrationState.Success)?.isRefreshing == true
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
