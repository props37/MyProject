package ru.livetyping.zarina.feature.cityselector.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavEntry
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavParams
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.CitySelectorScreen
import kotlin.reflect.KClass

public class CitySelectorFeatureImpl : CitySelectorFeature {
    override fun NavGraphBuilder.composable(
        actions: CitySelectorNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<CitySelectorNavEntry>(
            typeMap = CitySelectorNavEntry.typeMap(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            CitySelectorScreen(actions)
        }
    }

    override fun getNavEntry(params: CitySelectorNavParams): CitySelectorNavEntry {
        return params.toNavEntry()
    }

    override fun getNavEntryClass(): KClass<CitySelectorNavEntry> = CitySelectorNavEntry::class
}