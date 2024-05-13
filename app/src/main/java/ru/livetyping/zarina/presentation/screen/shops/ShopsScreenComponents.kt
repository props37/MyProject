package ru.livetyping.zarina.presentation.screen.shops

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.shop.Shop
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.map.GoogleMapsDefaults
import ru.livetyping.zarina.presentation.common.component.map.MapDefaults
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.domain.toLatLng
import ru.livetyping.zarina.presentation.screen.shops.ShopsViewModel.ShopListState
import ru.livetyping.zarina.presentation.screen.shops.ShopsViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

object ShopsScreenComponents {

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
                Text(text = stringResource(R.string.shops))
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ViewModePager(
        viewModes: ImmutableList<ViewMode>,
        pagerState: PagerState,
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        mapShopsState: ShopListState,
        shopListState: ShopListState,
        onShopsErrorRefreshClicked: () -> Unit,
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
                        mapShopsState = mapShopsState,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ViewMode.LIST -> {
                    ListViewMode(
                        shopListState = shopListState,
                        onShopsErrorRefreshClicked = onShopsErrorRefreshClicked,
                    )
                }
            }
        }
    }

    // TODO: [High] Handle clicks
    @OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
    @Composable
    private fun MapViewMode(
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        mapShopsState: ShopListState,
        modifier: Modifier = Modifier,
    ) {
        val coroutineScope = rememberCoroutineScope()

        val locationPermissions = remember {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
        val locationPermissionsState = rememberMultiplePermissionsState(locationPermissions)
        val isAnyLocationPermissionGranted by remember {
            derivedStateOf {
                locationPermissionsState.permissions.any { it.status.isGranted }
            }
        }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                Location.MOSCOW.toLatLng(),
                GoogleMapsDefaults.INITIAL_ZOOM,
            )
        }
        val properties = remember(isAnyLocationPermissionGranted) {
            MapProperties(
                isBuildingEnabled = true,
                isMyLocationEnabled = isAnyLocationPermissionGranted,
                maxZoomPreference = GoogleMapsDefaults.MAX_ZOOM_PREFERENCE,
            )
        }
        val uiSettings = remember {
            MapUiSettings(
                compassEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
            )
        }

        var previousLocation by remember { mutableStateOf<Location?>(null) }
        DisposableEffect(currentLocation) {
            if (currentLocation != null && previousLocation == null) {
                val newCameraPosition = CameraPosition.fromLatLngZoom(
                    currentLocation.toLatLng(),
                    GoogleMapsDefaults.CURRENT_LOCATION_ZOOM,
                )
                cameraPositionState.position = newCameraPosition
                previousLocation = currentLocation
            }

            onDispose {}
        }

        Box(modifier = modifier) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (mapShopsState is ShopListState.Success) {
                    val clusterItems = remember(mapShopsState.shops) {
                        mapShopsState.shops.map { ShopClusterItem(it) }
                    }
                    Clustering(
                        items = clusterItems,
                        clusterContent = { cluster ->
                            MapCluster(clusterSize = cluster.size)
                        },
                        clusterItemContent = {
                            Icon(
                                painter = painterResource(R.drawable.ic_map_shop_marker_24),
                                contentDescription = stringResource(
                                    id = R.string.map_shop_content_description,
                                    it.shop.name,
                                ),
                                tint = UiKitTheme.colors.icon.regular.default,
                                modifier = Modifier.size(36.dp),
                            )
                        },
                    )
                }
            }

            MapDefaults.MyLocationButton(
                onClick = {
                    onMyLocationClicked()

                    if (currentLocation != null) {
                        val zoom = cameraPositionState.position.zoom
                            .coerceAtLeast(GoogleMapsDefaults.CURRENT_LOCATION_ZOOM)
                        val newCameraPosition = CameraPosition.fromLatLngZoom(
                            currentLocation.toLatLng(),
                            zoom,
                        )
                        if (previousLocation != null) {
                            val update = CameraUpdateFactory.newCameraPosition(newCameraPosition)
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    update = update,
                                    durationMs = GoogleMapsDefaults.ANIMATION_DURATION_MILLIS,
                                )
                            }
                        } else {
                            cameraPositionState.position = newCameraPosition
                        }

                        previousLocation = currentLocation
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
            )
        }
    }

    @Composable
    private fun MapCluster(
        clusterSize: Int,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(52.dp)
                .border(
                    width = 1.dp,
                    color = UiKitTheme.colors.border.general.active,
                    shape = CircleShape,
                )
                .background(
                    color = UiKitTheme.colors.background.general.regular.default,
                    shape = CircleShape,
                ),
        ) {
            val text = if (clusterSize <= MapClusterMaxSize) {
                clusterSize.toString()
            } else {
                "$MapClusterMaxSize+"
            }
            Text(
                text = text,
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
            )
        }
    }

    @Composable
    private fun ListViewMode(
        shopListState: ShopListState,
        onShopsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = shopListState,
            contentKey = {
                when (it) {
                    is ShopListState.Success -> ShopListContentKeySuccess
                    ShopListState.Loading, is ShopListState.Error -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is ShopListState.Success -> {
                    ShopList(
                        shops = state.shops,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ShopListState.Loading -> {
                    ShopListSkeleton(modifier = Modifier.fillMaxSize())
                }

                is ShopListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onShopsErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopList(
        shops: ImmutableList<Shop>,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(
                items = shops,
                key = { _, shop -> shop.id.value },
            ) { index, shop ->
                ShopListItem(shop = shop)

                if (index < shops.lastIndex) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

        LazyColumn(modifier = modifier) {
            items(count = ShopListSkeletonItemCount) { index ->
                ShopListItemSkeleton(shimmer = shimmer)

                if (index < ShopListSkeletonItemCount - 1) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListItem(
        shop: Shop,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = ShopListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                Text(
                    text = shop.name,
                    style = ShopListItemNameTextStyle,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = shop.address,
                    style = ShopListItemInfoTextStyle,
                )
                
                if (shop.schedule != null) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = shop.schedule,
                        style = ShopListItemInfoTextStyle,
                    )
                }
            }
        }
    }

    @Composable
    private fun ShopListItemSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = ShopListItemContentPadding,
            modifier = modifier,
        ) {
            Column {
                ZarinaTextSkeleton(
                    textStyle = ShopListItemNameTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.4f),
                )

                Spacer(modifier = Modifier.height(6.dp))

                ZarinaTextSkeleton(
                    textStyle = ShopListItemInfoTextStyle,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                )
            }
        }
    }

    private data class ShopClusterItem(
        val shop: Shop,
    ) : ClusterItem {
        override fun getPosition(): LatLng = shop.location.toLatLng()

        override fun getTitle(): String? = null

        override fun getSnippet(): String? = null

        override fun getZIndex(): Float? = null
    }

    private const val ShopListContentKeySuccess = "ShopListContentKeySuccess"

    private const val ShopListSkeletonItemCount = 12

    private val ShopListItemContentPadding: PaddingValues get() = PaddingValues(16.dp)

    private val ShopListItemNameTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val ShopListItemInfoTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.tertiary.light

    private const val MapClusterMaxSize = 99
}
