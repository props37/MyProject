package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityNavActions

fun NavGraphBuilder.detectedCityFeature(
    feature: DetectedCityFeature,
    actions: DetectedCityNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberDetectedCityNavActions(
    navController: NavHostController
): DetectedCityNavActions {
    return remember(navController) {
        DetectedCityNavActions(
            onCloseClicked = { navController.navigateUp() },
        )
    }
}
