package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.ClickAction
import ru.livetyping.zarina.presentation.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.presentation.bottomnavbar.navigateToBottomNavBarItem
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.screen.home.HomeScreen
import ru.livetyping.zarina.presentation.screen.home.HomeScreenAction

fun NavGraphBuilder.homeScreen(navController: NavHostController) {
    composableDestination(
        destination = HomeGraph.Home,
        exitTransition = {
            when {
                targetState.destination.hasRoute<UnscopedDestinations.WebView>() -> {
                    slideExitTransition()
                }

                else -> null
            }
        },
        popEnterTransition = {
            when {
                initialState.destination.hasRoute<UnscopedDestinations.WebView>() -> {
                    slidePopEnterTransition()
                }

                else -> null
            }
        },
    ) {
        HomeScreen(
            navigateForward = { action ->
                when (action) {
                    is HomeScreenAction.BannerClicked -> {
                        when (val clickAction = action.banner.clickAction) {
                            is ClickAction.OpenProductList -> {
                                val categoryId = clickAction.categoryId
                                navController.navigateToProductsScreen(categoryId)
                            }

                            is ClickAction.OpenUrl -> {
                                val url = clickAction.url
                                val navEntry = UnscopedDestinations.WebView(url.value)
                                navController.navigate(navEntry)
                            }

                            null -> Unit
                        }
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToHomeScreen() {
    this.navigateToBottomNavBarItem(BottomNavBarItem.Home)
    this.popBackStack(
        route = HomeGraph.Home.routeSchema,
        inclusive = false,
    )
}
