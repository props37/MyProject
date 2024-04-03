package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.ui.navigation.screen.signUpScreen

fun NavGraphBuilder.signUpGraph(navController: NavHostController) {
    navigationGraph(SignUpGraph) {
        signUpScreen(navController)
    }
}

fun NavHostController.navigateToSignUpGraph() {
    this.navigate(SignUpGraph.route)
}
