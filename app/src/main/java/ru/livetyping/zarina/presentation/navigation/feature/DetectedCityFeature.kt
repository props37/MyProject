package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature

fun NavGraphBuilder.detectedCityFeature(
    feature: DetectedCityFeature,
    actions: DetectedCityFeature.NavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = EmptyNavResultRetrievers,
        )
    }
}

@Composable
fun rememberDetectedCityNavActions(
    navController: NavHostController
): DetectedCityFeature.NavActions {
    return remember(navController) {
        DetectedCityFeature.NavActions(
            onCloseClicked = { navController.navigateUp() },
        )
    }
}
