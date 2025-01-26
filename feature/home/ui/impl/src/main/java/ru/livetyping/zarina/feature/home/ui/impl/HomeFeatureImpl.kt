package ru.livetyping.zarina.feature.home.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.impl.HomeScreen

public class HomeFeatureImpl : HomeFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: HomeFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)?
    ) {
        navigation<HomeFeature.NavEntry>(
            startDestination = HomeFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            composable<HomeFeature.NavEntry.StartNavEntry> {
                HomeScreen(navActions = actions)
            }
        }
    }
}
