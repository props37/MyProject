package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.zarina.zarina.ui.navigation.util.BottomNavBarItemSecondaryStartDestinationBackHandler
import ru.zarina.zarina.ui.screen.profile.ProfileScreen

fun NavGraphBuilder.profileScreen(navController: NavHostController) {
    composableDestination(ProfileGraph.Profile) {
        BottomNavBarItemSecondaryStartDestinationBackHandler(navController)

        ProfileScreen()
    }
}
