package ru.livetyping.zarina.feature.search.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature
import ru.livetyping.zarina.feature.search.ui.impl.impl.navigation.searchScreen
import ru.livetyping.zarina.feature.search.ui.impl.impl.search.SearchNavActions

public class SearchFeatureImpl : SearchFeature {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        actions: SearchFeature.NavActions,
        resultRetrievers: EmptyNavResultRetrievers,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        navigation<SearchFeature.NavEntry>(
            startDestination = SearchFeature.NavEntry.StartNavEntry,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            sizeTransform = sizeTransform,
        ) {
            val searchNavActions = SearchNavActions(
                onBackClicked = actions.onBackClicked,
                onCategoryClicked = actions.onCategoryClicked,
                onProductClicked = actions.onProductClicked,
                onSubscribeToProductClicked = actions.onSubscribeToProductClicked,
            )
            searchScreen(searchNavActions)
        }
    }
}
