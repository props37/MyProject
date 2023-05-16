package ru.zarina.zarina.ui.screens.pickup.root.components

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import ru.zarina.zarina.domain.Stock

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
        )
    }
}
