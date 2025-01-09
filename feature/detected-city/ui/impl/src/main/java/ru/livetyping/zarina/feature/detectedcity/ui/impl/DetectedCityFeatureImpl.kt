package ru.livetyping.zarina.feature.detectedcity.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityNavActions
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityNavEntry
import ru.livetyping.zarina.feature.detectedcity.ui.impl.impl.DetectedCityScreen

public class DetectedCityFeatureImpl : DetectedCityFeature {
    override fun NavGraphBuilder.composable(
        actions: DetectedCityNavActions,
        resultRetrievers: Unit,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        dialog<DetectedCityNavEntry>(
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            DetectedCityScreen(actions)
        }
    }
}
