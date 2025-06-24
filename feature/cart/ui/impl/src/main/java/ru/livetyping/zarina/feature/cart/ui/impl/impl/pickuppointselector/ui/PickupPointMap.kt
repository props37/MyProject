package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uikit.map.ZarinaMapCluster
import ru.livetyping.zarina.core.uikit.map.ZarinaMapMarker
import ru.livetyping.zarina.core.uimap.GoogleMapsDefaults
import ru.livetyping.zarina.core.uimap.OptimizedClustering
import ru.livetyping.zarina.core.uimap.ZarinaGoogleMap
import ru.livetyping.zarina.core.uimap.toLatLng
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointListState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
internal fun PickupPointMap(
    state: PickupPointListState.Success,
    onPickupPointClicked: (PickupPoint) -> Unit,
    currentLocationProvider: () -> Location?,
    onMyLocationClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            Location.MOSCOW.toLatLng(),
            GoogleMapsDefaults.INITIAL_ZOOM,
        )
    }

    var mapSizePx by remember { mutableStateOf(IntSize.Zero) }

    ZarinaGoogleMap(
        currentLocation = currentLocationProvider(),
        onMyLocationClicked = onMyLocationClicked,
        cameraPositionState = cameraPositionState,
        myLocationButtonWindowInsetsProvider = { windowInsetsProvider() },
        modifier = modifier.onSizeChanged { mapSizePx = it },
    ) {
        val clusterItems = remember(state.pickupPoints) {
            state.pickupPoints.map { PickupPointClusterItem(it) }
        }

        OptimizedClustering(
            items = clusterItems,
            mapWidthPx = mapSizePx.width,
            mapHeightPx = mapSizePx.height,
            onClusterItemClick = { item ->
                onPickupPointClicked(item.pickupPoint)
                false
            },
            clusterContent = { cluster ->
                ZarinaMapCluster(clusterSize = cluster.size)
            },
            clusterItemContent = { item ->
                ZarinaMapMarker(contentDescription = item.pickupPoint.title)
            },
        )
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
