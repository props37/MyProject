package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.LoyaltyProgramGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryScreen
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryScreenAction

fun NavGraphBuilder.bonusHistoryScreen(navController: NavHostController) {
    composableDestination(
        destination = LoyaltyProgramGraph.BonusHistory,
        enterTransition = {
            when (initialState.destination.route) {
                LoyaltyProgramGraph.LoyaltyProgram.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                LoyaltyProgramGraph.LoyaltyProgram.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        BonusHistoryScreen(
            navigate = { action ->
                when (action) {
                    BonusHistoryScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = LoyaltyProgramGraph.BonusHistory.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToLoyaltyProgramBonusHistoryScreen() {
    this.navigate(LoyaltyProgramGraph.BonusHistory.route)
}
