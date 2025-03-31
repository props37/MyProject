package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.ViewMode

@Composable
internal fun ViewModeHorizontalPager(
    pagerState: PagerState,
    viewModes: ImmutableList<ViewMode>,
    pickupPointListState: PickupPointListState,
    pickupPointLazyListState: LazyListState,
    onPickupPointClicked: (PickupPoint) -> Unit,
    onErrorRefreshClicked: () -> Unit,
    currentLocationProvider: () -> Location?,
    onMyLocationClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        key = { page -> viewModes[page] },
        modifier = modifier,
    ) { page ->
        when (viewModes[page]) {
            ViewMode.MAP -> {
                PickupPointListScaffold(
                    state = pickupPointListState,
                    onErrorRefreshClicked = onErrorRefreshClicked,
                    successContent = { state ->
                        PickupPointMap(
                            state = state,
                            onPickupPointClicked = onPickupPointClicked,
                            currentLocationProvider = currentLocationProvider,
                            onMyLocationClicked = onMyLocationClicked,
                            windowInsetsProvider = windowInsetsProvider,
                        )
                    },
                    loadingContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(windowInsetsProvider()),
                        ) {
                            ZarinaCircularLoader(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(40.dp),
                            )
                        }
                    },
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            ViewMode.LIST -> {
                // TODO: [Top] Implement
            }
        }
    }
}
