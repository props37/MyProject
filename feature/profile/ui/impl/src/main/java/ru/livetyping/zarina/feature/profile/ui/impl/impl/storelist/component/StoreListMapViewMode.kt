package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListState

@Composable
internal fun StoreListMapViewMode(
    onStoreListEvent: (StoreListEvent) -> Unit,
    mapStateProvider: () -> StoreListState,
    currentLocationProvider: () -> Location?,
    onStoreClicked: (Store) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = mapStateProvider(),
        contentKey = {
            when (it) {
                is StoreListState.Success -> StoreListMapViewModeContentKey.Success
                is StoreListState.Error, StoreListState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is StoreListState.Success -> {
                StoreMapSuccess(
                    storeMapState = state,
                    currentLocationProvider = currentLocationProvider,
                    onStoreClicked = onStoreClicked,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            StoreListState.Loading -> {
                StoreMapLoading(modifier = Modifier.fillMaxSize())
            }

            is StoreListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = { onStoreListEvent(StoreListEvent.ErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreMapSuccess(
    storeMapState: StoreListState.Success,
    currentLocationProvider: () -> Location?,
    onStoreClicked: (Store) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: [Top] Implement
}

@Composable
private fun StoreMapLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        ZarinaCircularLoader(modifier = Modifier.size(40.dp))
    }
}

private enum class StoreListMapViewModeContentKey { Success }
