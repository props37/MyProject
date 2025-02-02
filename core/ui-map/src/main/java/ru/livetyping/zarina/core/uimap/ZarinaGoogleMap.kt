package ru.livetyping.zarina.core.uimap

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.map.ZarinaMapMyLocationButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
public fun ZarinaGoogleMap(
    currentLocation: Location?,
    onMyLocationClicked: () -> Unit,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    myLocationButtonWindowInsetsProvider: () -> WindowInsets = { WindowInsets.none },
    content: @Composable @GoogleMapComposable () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val locationPermissions = remember {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }
    // TODO: [High] Migrate to ActivityResult API
    val locationPermissionsState = rememberMultiplePermissionsState(locationPermissions)
    val isAnyLocationPermissionGranted by remember {
        derivedStateOf {
            locationPermissionsState.permissions.any { it.status.isGranted }
        }
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
            content = content,
            modifier = Modifier.fillMaxSize(),
        )

        ZarinaMapMyLocationButton(
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
                .padding(end = MyLocationButtonPadding, bottom = MyLocationButtonPadding)
                .windowInsetsPadding(myLocationButtonWindowInsetsProvider()),
        )
    }
}

private val MyLocationButtonPadding: Dp get() = 16.dp
