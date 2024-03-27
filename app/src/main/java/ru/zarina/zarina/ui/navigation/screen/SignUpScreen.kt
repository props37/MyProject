package ru.zarina.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.zarina.zarina.ui.screen.signup.SignUpScreen

fun NavGraphBuilder.signUpScreen(navController: NavHostController) {
    composableDestination(ProfileGraph.SignUp) {
        SignUpScreen()
    }
}
