package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uimap.GoogleMapsDefaults
import ru.livetyping.zarina.core.uimap.MapCluster
import ru.livetyping.zarina.core.uimap.MapMarker
import ru.livetyping.zarina.core.uimap.ZarinaGoogleMap
import ru.livetyping.zarina.core.uimap.toLatLng
import ru.livetyping.zarina.feature.profile.ui.impl.R
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
                    onMyLocationClicked = { onStoreListEvent(StoreListEvent.MyLocationClicked) },
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

@OptIn(MapsComposeExperimentalApi::class)
@Composable
private fun StoreMapSuccess(
    storeMapState: StoreListState.Success,
    currentLocationProvider: () -> Location?,
    onMyLocationClicked: () -> Unit,
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
        currentLocation = currentLocationProvider(),
        onMyLocationClicked = onMyLocationClicked,
        cameraPositionState = cameraPositionState,
        modifier = modifier,
    ) {
        val stores = storeMapState.stores
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
                MapCluster(itemCount = cluster.size)
            },
            clusterItemContent = { item ->
                MapMarker(
                    contentDescription = stringResource(
                        id = R.string.profile_map_store_content_description,
                        item.store.name,
                    ),
                )
            },
        )
    }
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

private data class StoreClusterItem(val store: Store) : ClusterItem {
    override fun getPosition(): LatLng = store.location.toLatLng()

    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null

    override fun getZIndex(): Float? = null
}
