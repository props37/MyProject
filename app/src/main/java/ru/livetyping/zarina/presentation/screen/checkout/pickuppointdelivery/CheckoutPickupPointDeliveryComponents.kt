package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import android.Manifest
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.map.GoogleMapsDefaults
import ru.livetyping.zarina.presentation.common.component.map.MapDefaults
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTag
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.util.domain.toLatLng
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.Filter
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.PickupPointsState
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ToggleableFilter
import ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery.CheckoutPickupPointDeliveryViewModel.ViewMode
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CheckoutPickupPointDeliveryComponents {

    @Composable
    fun FilterBlock(
        nameOrAddressFilterTextFieldState: TextFieldState,
        filters: List<ToggleableFilter>,
        onFilterClicked: (ToggleableFilter) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            var focusState by remember { mutableStateOf<FocusState?>(null) }
            ZarinaTextField(
                state = nameOrAddressFilterTextFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                leadingContent = {
                   Icon(
                       imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                       contentDescription = null,
                       modifier = Modifier.size(16.dp),
                   )
                },
                placeholder = {
                    Text(text = stringResource(R.string.address_or_name))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = nameOrAddressFilterTextFieldState.text.isNotBlank(),
                        onClick = { nameOrAddressFilterTextFieldState.clearText() },
                    )
                },
                outerTrailingContent = {
                    val focusManager = LocalFocusManager.current
                    ZarinaTextFieldDefaults.CancelButton(
                        isVisible = focusState?.isFocused == true,
                        onClick = { focusManager.clearFocus() },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState = it },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                filters.forEach { filter ->
                    ZarinaTag(
                        onClick = { onFilterClicked(filter) },
                        isSelected = filter.isApplied,
                    ) {
                        val textResId = when (filter.filter) {
                            Filter.PAYMENT_BY_CARD -> R.string.payment_by_card
                            Filter.FITTING -> R.string.with_fitting
                        }

                        Text(text = stringResource(textResId))
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

    @Composable
    fun ViewModeTabRow(
        modes: List<ViewMode>,
        currentMode: ViewMode,
        onModeChanged: (ViewMode) -> Unit,
        onCurrentModeClicked: (ViewMode) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = modes.indexOf(currentMode),
            modifier = modifier,
        ) {
            modes.forEach { mode ->
                val textResId = when (mode) {
                    ViewMode.MAP -> R.string.map
                    ViewMode.LIST -> R.string.list
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    isSelected = mode == currentMode,
                    onClick = {
                        if (mode == currentMode) {
                            onCurrentModeClicked(mode)
                        } else {
                            onModeChanged(mode)
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun ViewModeHorizontalPager(
        pagerState: PagerState,
        viewModes: List<ViewMode>,
        pickupPointsState: PickupPointsState,
        onPickupPointClicked: (PickupPoint) -> Unit,
        pickupPointsListState: LazyListState,
        onPickupPointsErrorRefreshClicked: () -> Unit,
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            when (viewModes[page]) {
                ViewMode.MAP -> {
                    PickupPointStateScaffold(
                        pickupPointsState = pickupPointsState,
                        onErrorRefreshClicked = onPickupPointsErrorRefreshClicked,
                        successContent = {
                            PickupPointMap(
                                currentLocation = currentLocation,
                                onMyLocationClicked = onMyLocationClicked,
                                pickupPointsState = it,
                                onPickupPointClicked = onPickupPointClicked,
                            )
                        },
                        loadingContent = {
                            Box(modifier = Modifier.fillMaxSize()) {
                                ZarinaCircularLoader(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(40.dp),
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                ViewMode.LIST -> {
                    PickupPointStateScaffold(
                        pickupPointsState = pickupPointsState,
                        onErrorRefreshClicked = onPickupPointsErrorRefreshClicked,
                        successContent = {
                            PickupPointList(
                                pickupPointsState = it,
                                onPickupPointClicked = onPickupPointClicked,
                                lazyListState = pickupPointsListState,
                            )
                        },
                        loadingContent = {
                            PickupPointListSkeleton()
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    @Composable
    private fun PickupPointStateScaffold(
        pickupPointsState: PickupPointsState,
        onErrorRefreshClicked: () -> Unit,
        successContent: @Composable (PickupPointsState.Success) -> Unit,
        loadingContent: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = pickupPointsState,
            contentKey = {
                when (it) {
                    is PickupPointsState.Success -> PickupPointStateScaffoldContentKeySuccess
                    is PickupPointsState.Error -> it
                    PickupPointsState.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is PickupPointsState.Success -> {
                    successContent(state)
                }

                PickupPointsState.Loading -> {
                    loadingContent()
                }

                is PickupPointsState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
    @Composable
    private fun PickupPointMap(
        currentLocation: Location?,
        onMyLocationClicked: () -> Unit,
        pickupPointsState: PickupPointsState.Success,
        onPickupPointClicked: (PickupPoint) -> Unit,
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
                val clusterItems = remember(pickupPointsState.pickupPoints) {
                    pickupPointsState.pickupPoints.map { PickupPointClusterItem(it) }
                }
                Clustering(
                    items = clusterItems,
                    onClusterItemClick = { item ->
                        onPickupPointClicked(item.pickupPoint)
                        false
                    },
                    clusterContent = { cluster ->
                        MapDefaults.Cluster(clusterSize = cluster.size)
                    },
                    clusterItemContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_map_store_marker_24),
                            contentDescription = stringResource(
                                id = R.string.map_store_content_description,
                                it.pickupPoint.title,
                            ),
                            tint = UiKitTheme.colors.icon.regular.default,
                            modifier = Modifier.size(36.dp),
                        )
                    },
                )
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
                    .padding(end = 16.dp, bottom = 16.dp)
                    .safeDrawingPadding(),
            )
        }
    }

    @Composable
    private fun PickupPointList(
        pickupPointsState: PickupPointsState.Success,
        onPickupPointClicked: (PickupPoint) -> Unit,
        lazyListState: LazyListState,
        modifier: Modifier = Modifier,
    ) {
        LaunchedEffect(pickupPointsState.pickupPoints, lazyListState) {
            lazyListState.scrollToItem(0)
        }

        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = 20.dp + navigationBarHeight)

        LazyColumn(
            state = lazyListState,
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = pickupPointsState.pickupPoints,
                key = { _, pickupPoint -> pickupPoint.id.value },
            ) { index, pickupPoint ->
                Column(modifier = Modifier.animateItem()) {
                    PickupPoint(
                        pickupPoint = pickupPoint,
                        onClick = { onPickupPointClicked(pickupPoint) },
                    )

                    if (index < pickupPointsState.pickupPoints.lastIndex) {
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
    private fun PickupPointListSkeleton(
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

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(20.dp + navigationBarHeight))
        }
    }

    @Composable
    private fun PickupPoint(
        pickupPoint: PickupPoint,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            onClick = onClick,
            startContent = {
                Column {
                    Text(
                        text = pickupPoint.title,
                        style = UiKitTheme.typography.secondary.light,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = pickupPoint.title,
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            },
            endContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            },
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            modifier = modifier,
        )
    }

    @Composable
    private fun PickupPointSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            modifier = modifier,
        ) {
            Column {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.light,
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    shimmer = shimmer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.footnote.light,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                    shimmer = shimmer,
                )
            }
        }
    }

    private data class PickupPointClusterItem(
        val pickupPoint: PickupPoint,
    ) : ClusterItem {
        override fun getPosition(): LatLng = pickupPoint.location.toLatLng()

        override fun getTitle(): String? = null
        override fun getSnippet(): String? = null
        override fun getZIndex(): Float? = null
    }

    private const val PickupPointStateScaffoldContentKeySuccess =
        "PickupPointStateScaffoldContentKeySuccess"

    private const val PickupPointListSkeletonCount = 10
}
