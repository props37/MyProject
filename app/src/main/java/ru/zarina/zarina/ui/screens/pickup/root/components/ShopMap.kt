package ru.zarina.zarina.ui.screens.pickup.root.components

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.ui.common.utils.domain.toLatLng
import ru.zarina.zarina.utils.maps.getBitmapDescriptor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShopMap(
    stocks: List<Stock>,
    isVisibleForUser: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isVisibleForUser) {
        val fineLocationPermissionState =
            rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
        LaunchedEffect(Unit) {
            fineLocationPermissionState.launchPermissionRequest()
        }
    }
    val anyLocationPermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val properties = MapProperties(
            isBuildingEnabled = true,
            isMyLocationEnabled = anyLocationPermissionState.permissions.any { it.status.isGranted }
        )
        GoogleMap(
            properties = properties,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            val pinBitmapDescriptor =
                remember(context) { context.getBitmapDescriptor(R.drawable.ic_map_pin_marker) }
            stocks.forEach { stock ->
                Marker(
                    state = MarkerState(position = stock.shop.geoLocation.toLatLng()),
                    icon = pinBitmapDescriptor,
                )
            }
        }
    }
}
