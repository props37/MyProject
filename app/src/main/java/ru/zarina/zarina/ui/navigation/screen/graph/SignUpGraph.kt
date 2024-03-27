package ru.zarina.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.zarina.zarina.ui.navigation.screen.signUpScreen

fun NavGraphBuilder.signUpGraph(navController: NavHostController) {
    navigationGraph(SignUpGraph) {
        signUpScreen(navController)
    }
}
