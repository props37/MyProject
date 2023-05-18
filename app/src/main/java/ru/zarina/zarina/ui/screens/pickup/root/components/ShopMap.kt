package ru.zarina.zarina.ui.screens.pickup.root.components

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.ui.common.utils.domain.toLatLng
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.maps.getBitmapDescriptor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShopMap(
    stocks: ImmutableList<Stock>,
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
        var isInitialMoveCompleted by remember { mutableStateOf(false) }
        val properties = MapProperties(
            isMyLocationEnabled = anyLocationPermissionState.permissions.any { it.status.isGranted }
        )
        val cameraState = rememberCameraPositionState()
        val boundsPadding = with(LocalDensity.current) { 32.dp.roundToPx() }
        LaunchedEffect(stocks) {
            if (stocks.isEmpty()) return@LaunchedEffect
            val update = if (stocks.size == 1) {
                CameraUpdateFactory.newLatLngZoom(stocks.first().shop.geoLocation.toLatLng(), 13f)
            } else {
                val bounds = LatLngBounds.builder()
                    .apply { stocks.forEach { include(it.shop.geoLocation.toLatLng()) } }
                    .build()

                CameraUpdateFactory.newLatLngBounds(bounds, boundsPadding)
            }
            if (isInitialMoveCompleted) {
                cameraState.animate(update)
            } else {
                cameraState.move(update)
                isInitialMoveCompleted = true
            }
        }
        GoogleMap(
            cameraPositionState = cameraState,
            properties = properties,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            val pinBitmapDescriptor =
                remember(context) { context.getBitmapDescriptor(R.drawable.ic_map_pin_marker) }
            stocks.forEach { stock ->
                MarkerInfoWindowContent(
                    state = MarkerState(position = stock.shop.geoLocation.toLatLng()),
                    icon = pinBitmapDescriptor,
                ) {
                    Box(
                        modifier = Modifier
                            .background(UiKitTheme.colors.screenBackground)
                            .padding(8.dp)
                    ) {
                        ShopItem(stock, {}, isButtonVisible = false)
                    }
                }
            }
        }
    }
}
