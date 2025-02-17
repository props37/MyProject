package ru.livetyping.zarina.core.uicomponent.filtration.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationState
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader

@Suppress("NAME_SHADOWING")
@Composable
internal fun Filters(
    state: FiltrationState,
    onEvent: (FiltrationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is FiltrationState.Success -> FiltersContentKey.Success
                is FiltrationState.Error, FiltrationState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is FiltrationState.Success -> {
                FiltersSuccess(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FiltrationState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                }
            }

            is FiltrationState.Error -> {
                ZarinaErrorScreen(
                    state = state.errorState,
                    onButtonClicked = { onEvent(FiltrationEvent.FiltrationErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

private enum class FiltersContentKey { Success }
