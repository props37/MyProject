package ru.zarina.zarina.ui.navigation.rework.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.rework.destination.ProfileGraph
import ru.zarina.zarina.ui.navigation.rework.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.profile.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigationGraph(ProfileGraph) {
        composableDestination(ProfileGraph.Profile) {
            BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

            ProfileScreen()
        }
    }
}
