package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.LoyaltyProgramGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreen
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramScreenAction

fun NavGraphBuilder.loyaltyProgramScreen(navController: NavHostController) {
    composableDestination(
        destination = LoyaltyProgramGraph.LoyaltyProgram,
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
        LoyaltyProgramScreen(
            navigate = { action ->
                when (action) {
                    LoyaltyProgramScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = LoyaltyProgramGraph.LoyaltyProgram.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
