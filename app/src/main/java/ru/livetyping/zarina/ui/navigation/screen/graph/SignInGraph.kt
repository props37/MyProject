package ru.livetyping.zarina.ui.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.navigationGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.ui.navigation.screen.passwordRecoveryScreen
import ru.livetyping.zarina.ui.navigation.screen.signInOtpScreen
import ru.livetyping.zarina.ui.navigation.screen.signInScreen

fun NavGraphBuilder.signInGraph(navController: NavHostController) {
    navigationGraph(SignInGraph) {
        signInScreen(navController)
        passwordRecoveryScreen(navController)
        signInOtpScreen(navController)
    }
}

fun NavHostController.navigateToSignInGraph() {
    this.navigate(SignInGraph.route)
}
