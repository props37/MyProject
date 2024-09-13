package ru.livetyping.zarina.presentation.screen.stores

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.animation.LazyListFadeInSpec
import ru.livetyping.zarina.presentation.common.animation.LazyListFadeOutSpec
import ru.livetyping.zarina.presentation.common.animation.LazyListPlacementSpec
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.map.GoogleMapsDefaults
import ru.livetyping.zarina.presentation.common.component.map.MapDefaults
import ru.livetyping.zarina.presentation.common.component.map.ZarinaGoogleMap
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.domain.toLatLng
import ru.livetyping.zarina.presentation.screen.stores.StoresViewModel.StoreListState
import ru.livetyping.zarina.presentation.screen.stores.StoresViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object StoresScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.stores),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun ViewModeTabRow(
        viewModes: ImmutableList<ViewMode>,
        currentViewMode: ViewMode,
        onViewModeChanged: (ViewMode) -> Unit,
        viewModePagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = viewModePagerState.currentPage,
            modifier = modifier,
        ) {
            viewModes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }
                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = { onViewModeChanged(mode) },
                    isSelected = mode == currentViewMode,
                )
            }
        }
    }

    @Composable
    fun ViewModePager(
        viewModes: ImmutableList<ViewMode>,
        pagerState: PagerState,
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        storeMapState: StoreListState,
        storeListState: StoreListState,
        onStoresErrorRefreshClicked: () -> Unit,
        onStoreClicked: (Store) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            when (viewModes[page]) {
                ViewMode.MAP -> {
                    MapViewMode(
                        currentLocation = currentLocation,
                        onMyLocationClicked = onMyLocationClicked,
                        storeMapState = storeMapState,
                        onStoresErrorRefreshClicked = onStoresErrorRefreshClicked,
                        onStoreClicked = onStoreClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ViewMode.LIST -> {
                    ListViewMode(
                        storeListState = storeListState,
                        onStoresErrorRefreshClicked = onStoresErrorRefreshClicked,
                    )
                }
            }
        }
    }

    @Composable
    private fun MapViewMode(
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        storeMapState: StoreListState,
        onStoresErrorRefreshClicked: () -> Unit,
        onStoreClicked: (Store) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = storeMapState,
            contentKey = {
                when (it) {
                    is StoreListState.Success -> StoreMapContentKeySuccess
                    is StoreListState.Error, StoreListState.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is StoreListState.Success -> {
                    StoreMap(
                        currentLocation = currentLocation,
                        onMyLocationClicked = onMyLocationClicked,
                        stores = state.stores,
                        onStoreClicked = onStoreClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                StoreListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ZarinaCircularLoader(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(40.dp),
                        )
                    }
                }

                is StoreListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onStoresErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @OptIn(MapsComposeExperimentalApi::class)
    @Composable
    private fun StoreMap(
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        stores: ImmutableList<Store>,
        onStoreClicked: (Store) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                Location.MOSCOW.toLatLng(),
                GoogleMapsDefaults.INITIAL_ZOOM,
            )
        }

        ZarinaGoogleMap(
            currentLocation = currentLocation,
            onMyLocationClicked = onMyLocationClicked,
            cameraPositionState = cameraPositionState,
            modifier = modifier,
        ) {
            val clusterItems = remember(stores) {
                stores.map { StoreClusterItem(it) }
            }

            Clustering(
                items = clusterItems,
                onClusterItemClick = { item ->
                    onStoreClicked(item.store)
                    false
                },
                clusterContent = { cluster ->
                    MapDefaults.Cluster(clusterSize = cluster.size)
                },
                clusterItemContent = {
                    MapDefaults.ClusterIcon(
                        contentDescription = stringResource(
                            id = R.string.map_store_content_description,
                            it.store.name,
                        ),
                    )
                },
            )
        }
    }

    @Composable
    private fun ListViewMode(
        storeListState: StoreListState,
        onStoresErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = storeListState,
            contentKey = {
                when (it) {
                    is StoreListState.Success -> StoreListContentKeySuccess
                    StoreListState.Loading, is StoreListState.Error -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is StoreListState.Success -> {
                    StoreList(
                        stores = state.stores,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                StoreListState.Loading -> {
                    StoreListSkeleton(modifier = Modifier.fillMaxSize())
                }

                is StoreListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onStoresErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun StoreList(
        stores: ImmutableList<Store>,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            itemsIndexed(
                items = stores,
                key = { _, store -> store.id.value },
            ) { index, store ->
                Column(
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeOutSpec,
                    ),
                ) {
                    StoreListItem(store = store)

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
    }

    @Composable
    private fun StoreListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            items(count = StoreListSkeletonItemCount) { index ->
                Column(
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeOutSpec,
                    ),
                ) {
                    StoreListItemSkeleton(shimmer = shimmer)

                    if (index < StoreListSkeletonItemCount - 1) {
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
    private fun StoreListItem(
        store: Store,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = StoreListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                Text(
                    text = store.name,
                    style = StoreListItemNameTextStyle,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = store.address,
                    style = StoreListItemInfoTextStyle,
                )
                
                if (store.schedule != null) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = store.schedule,
                        style = StoreListItemInfoTextStyle,
                    )
                }
            }
        }
    }

    @Composable
    private fun StoreListItemSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = StoreListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                ZarinaTextSkeleton(
                    textStyle = StoreListItemNameTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.4f),
                )

                Spacer(modifier = Modifier.height(6.dp))

                ZarinaTextSkeleton(
                    textStyle = StoreListItemInfoTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                )
            }
        }
    }

    private data class StoreClusterItem(
        val store: Store,
    ) : ClusterItem {
        override fun getPosition(): LatLng = store.location.toLatLng()

        override fun getTitle(): String? = null

        override fun getSnippet(): String? = null

        override fun getZIndex(): Float? = null
    }

    private const val StoreMapContentKeySuccess = "StoreMapContentKeySuccess"

    private const val StoreListContentKeySuccess = "StoreListContentKeySuccess"

    private const val StoreListSkeletonItemCount = 12

    private val StoreListItemContentPadding: PaddingValues get() = PaddingValues(16.dp)

    private val StoreListItemNameTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val StoreListItemInfoTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.tertiary.light
}
