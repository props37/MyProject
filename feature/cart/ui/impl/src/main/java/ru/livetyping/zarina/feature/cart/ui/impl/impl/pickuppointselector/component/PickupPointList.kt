package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListState

@Composable
internal fun PickupPointList(
    state: PickupPointListState.Success,
    onPickupPointClicked: (PickupPoint) -> Unit,
    lazyListState: LazyListState,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    DisposableEffect(state.pickupPoints, lazyListState) {
        lazyListState.requestScrollToItem(0)
        onDispose {}
    }

    val windowInsetsBottomHeight = windowInsetsProvider()
        .asPaddingValues()
        .calculateBottomPadding()
    val contentPadding = PaddingValues(
        bottom = windowInsetsBottomHeight + ZarinaScrollableDefaults.ScrollableBottomPadding,
    )

    LazyColumn(
        state = lazyListState,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = state.pickupPoints,
            key = { _, pickupPoint -> pickupPoint.id.value },
        ) { index, pickupPoint ->
            Column(modifier = Modifier.animateZarinaItem(this)) {
                PickupPoint(
                    pickupPoint = pickupPoint,
                    onClick = { onPickupPointClicked(pickupPoint) },
                )

                if (index < state.pickupPoints.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun PickupPointListLoading(
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer(bounds = ShimmerBounds.Window)
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        repeat(PickupPointListSkeletonCount) { index ->
            PickupPointSkeleton(shimmer = shimmer)

            if (index < PickupPointListSkeletonCount - 1) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
    }
}

private const val PickupPointListSkeletonCount = 10
