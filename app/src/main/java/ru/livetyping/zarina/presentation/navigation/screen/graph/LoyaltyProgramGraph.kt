package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.LoyaltyProgramGraph
import ru.livetyping.zarina.presentation.navigation.screen.bonusHistoryScreen
import ru.livetyping.zarina.presentation.navigation.screen.loyaltyProgramScreen

fun NavGraphBuilder.loyaltyProgramGraph(navController: NavHostController) {
    navigationGraph(LoyaltyProgramGraph) {
        loyaltyProgramScreen(navController)
        bonusHistoryScreen(navController)
    }
}

fun NavHostController.navigateToLoyaltyProgramGraph() {
    this.navigate(LoyaltyProgramGraph.route)
}
