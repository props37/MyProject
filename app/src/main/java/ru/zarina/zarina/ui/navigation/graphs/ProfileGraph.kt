package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Profile
import ru.zarina.zarina.ui.screens.profile.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigationGraph(Profile) {
        composableDestination(Profile.Root) {
            ProfileScreen()
        }
    }
}
