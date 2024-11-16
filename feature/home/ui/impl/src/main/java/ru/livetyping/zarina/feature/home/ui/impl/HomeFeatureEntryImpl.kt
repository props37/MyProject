package ru.livetyping.zarina.feature.home.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.HomeNavActions
import ru.livetyping.zarina.feature.home.ui.HomeNavEntry
import ru.livetyping.zarina.feature.home.ui.HomeNavParams
import ru.livetyping.zarina.feature.home.ui.impl.impl.HomeScreen

public class HomeFeatureEntryImpl : HomeFeatureEntry {
    override fun NavGraphBuilder.composable(
        actions: HomeNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<HomeNavEntry> {
            HomeScreen(navActions = actions)
        }
    }

    override fun getNavEntry(params: HomeNavParams): HomeNavEntry = HomeNavEntry
}
