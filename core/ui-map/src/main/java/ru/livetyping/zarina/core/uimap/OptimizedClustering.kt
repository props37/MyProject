package ru.livetyping.zarina.core.uimap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer

@MapsComposeExperimentalApi
@Composable
public fun <T : ClusterItem> OptimizedClustering(
    items: List<T>,
    mapWidthPx: Int,
    mapHeightPx: Int,
    onClusterClick: (Cluster<T>) -> Boolean = { false },
    onClusterItemClick: (T) -> Boolean = { false },
    onClusterItemInfoWindowClick: (T) -> Unit = {},
    onClusterItemInfoWindowLongClick: (T) -> Unit = {},
    clusterContent: @Composable ((Cluster<T>) -> Unit)? = null,
    clusterItemContent: @Composable ((T) -> Unit)? = null,
) {
    val clusterManager = rememberClusterManager<T>()
    val renderer = rememberClusterRenderer(
        clusterContent = clusterContent,
        clusterItemContent = clusterItemContent,
        clusterManager = clusterManager,
    )

    // Set algorithm
    DisposableEffect(clusterManager, mapWidthPx, mapHeightPx) {
        clusterManager?.setAlgorithm(
            NonHierarchicalViewBasedAlgorithm(mapWidthPx, mapHeightPx)
        )
        onDispose {}
    }

    // Set click listeners
    val currentOnClusterClick by rememberUpdatedState(onClusterClick)
    val currentOnClusterItemClick by rememberUpdatedState(onClusterItemClick)
    val currentOnClusterItemInfoWindowClick by rememberUpdatedState(onClusterItemInfoWindowClick)
    val currentOnClusterItemInfoWindowLongClick by rememberUpdatedState(
        onClusterItemInfoWindowLongClick
    )
    DisposableEffect(clusterManager) {
        clusterManager?.setOnClusterClickListener(currentOnClusterClick)
        clusterManager?.setOnClusterItemClickListener(currentOnClusterItemClick)
        clusterManager?.setOnClusterItemInfoWindowClickListener(currentOnClusterItemInfoWindowClick)
        clusterManager?.setOnClusterItemInfoWindowLongClickListener(
            currentOnClusterItemInfoWindowLongClick
        )
        onDispose {}
    }

    // Set renderer
    DisposableEffect(clusterManager, renderer) {
        if (renderer != null && clusterManager?.renderer != renderer) {
            clusterManager?.renderer = renderer
        }
        onDispose {}
    }

    if (clusterManager != null) {
        Clustering(
            items = items,
            clusterManager = clusterManager,
        )
    }
}
