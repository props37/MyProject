package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.shops.ShopsScreen
import ru.livetyping.zarina.ui.screen.shops.ShopsScreenAction

fun NavGraphBuilder.shopsScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.Shops,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ShopsScreen(
            navigate = { action ->
                when (action) {
                    ShopsScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.Shops.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToShopsScreen() {
    this.navigate(ProfileGraph.Shops.route)
}
