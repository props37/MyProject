package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.ProfileGraph

fun NavGraphBuilder.signUpScreen(navController: NavHostController) {
    composableDestination(ProfileGraph.SignUp) {
        // TODO: [High] Implement
    }
}
