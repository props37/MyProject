package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model.PickupStoreSelectorState

@Composable
internal fun PickupStoreSelector(
    state: PickupStoreSelectorState,
    onStoreClicked: (PickupStore) -> Unit,
    onStoresErrorRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Column(modifier = modifier) {
        val city = (state as? PickupStoreSelectorState.Success)?.city
        if (city != null) {
            City(city)
        }

        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is PickupStoreSelectorState.Success -> StoreListContentKey.Success
                    is PickupStoreSelectorState.Error -> it
                    PickupStoreSelectorState.Loading -> it
                }
            },
        ) { state ->
            when (state) {
                is PickupStoreSelectorState.Success -> {
                    StoreList(
                        stores = state.stores,
                        cartItemCount = state.cartItemCount,
                        onStoreClicked = onStoreClicked,
                        windowInsetsProvider = windowInsetsProvider,
                    )
                }

                PickupStoreSelectorState.Loading -> {
                    StoreListLoading(windowInsetsProvider = windowInsetsProvider)
                }

                is PickupStoreSelectorState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onStoresErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(windowInsetsProvider())
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreList(
    stores: ImmutableList<PickupStore>,
    cartItemCount: Int,
    onStoreClicked: (PickupStore) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val windowInsetsBottomHeight = windowInsetsProvider().asPaddingValues().calculateBottomPadding()
    val contentPadding = PaddingValues(
        bottom = windowInsetsBottomHeight + ZarinaScrollableDefaults.ScrollableBottomPadding,
    )

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = stores,
            key = { _, store -> store.store.id.value },
        ) { index, store ->
            Store(
                store = store,
                cartItemCount = cartItemCount,
                onClick = onStoreClicked,
            )

            if (index < stores.lastIndex) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreListLoading(
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
        repeat(StoreListSkeletonCount) { index ->
            StoreSkeleton(shimmer = shimmer)

            if (index < StoreListSkeletonCount - 1) {
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

private enum class StoreListContentKey { Success }

private const val StoreListSkeletonCount = 6
