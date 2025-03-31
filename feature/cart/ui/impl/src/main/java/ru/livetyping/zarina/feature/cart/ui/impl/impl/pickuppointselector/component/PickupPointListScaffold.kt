package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListState

@Suppress("NAME_SHADOWING")
@Composable
internal fun PickupPointListScaffold(
    state: PickupPointListState,
    onErrorRefreshClicked: () -> Unit,
    successContent: @Composable (PickupPointListState.Success) -> Unit,
    loadingContent: @Composable () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is PickupPointListState.Success -> ContentKey.Success
                is PickupPointListState.Error -> it
                PickupPointListState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is PickupPointListState.Success -> {
                successContent(state)
            }

            PickupPointListState.Loading -> {
                loadingContent()
            }

            is PickupPointListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = onErrorRefreshClicked,
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
