package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.screen.myOrdersScreen
import ru.livetyping.zarina.ui.navigation.screen.profileDetailsScreen
import ru.livetyping.zarina.ui.navigation.screen.profileScreen

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigationGraph(ProfileGraph) {
        profileScreen(navController)
        profileDetailsScreen(navController)
        myOrdersScreen(navController)
    }
}
