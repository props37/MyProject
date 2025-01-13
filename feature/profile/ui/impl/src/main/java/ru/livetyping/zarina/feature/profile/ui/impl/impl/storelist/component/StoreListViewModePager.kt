package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListViewMode

@Composable
internal fun StoreListViewModePager(
    viewModes: ImmutableList<StoreListViewMode>,
    pagerState: PagerState,
    onStoreListEvent: (StoreListEvent) -> Unit,
    mapStateProvider: () -> StoreListState,
    listStateProvider: () -> StoreListState,
    currentLocationProvider: () -> Location?,
    onStoreClicked: (Store) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = false,
        key = { page -> viewModes[page] },
        modifier = modifier,
    ) { page ->
        when (viewModes[page]) {
            StoreListViewMode.MAP -> {
                StoreListMapViewMode(
                    onStoreListEvent = onStoreListEvent,
                    mapStateProvider = mapStateProvider,
                    currentLocationProvider = currentLocationProvider,
                    onStoreClicked = onStoreClicked,
                )
            }

            StoreListViewMode.LIST -> {
                StoreListListViewMode(
                    onStoreListEvent = onStoreListEvent,
                    listStateProvider = listStateProvider,
                )
            }
        }
    }
}
