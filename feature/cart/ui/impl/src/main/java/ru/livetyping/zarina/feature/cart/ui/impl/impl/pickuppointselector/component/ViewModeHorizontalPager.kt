package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
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
                // TODO: [Top] Implement
            }

            ViewMode.LIST -> {
                // TODO: [Top] Implement
            }
        }
    }
}
