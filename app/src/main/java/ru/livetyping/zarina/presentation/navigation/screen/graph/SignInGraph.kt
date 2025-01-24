package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.screen.passwordRecoveryScreen
import ru.livetyping.zarina.presentation.navigation.screen.phoneNumberConfirmationScreen
import ru.livetyping.zarina.presentation.navigation.screen.signInOtpScreen
import ru.livetyping.zarina.presentation.navigation.screen.signInScreen

fun NavGraphBuilder.signInGraph(navController: NavHostController) {
    navigationGraph(SignInGraph) {
        signInScreen(navController)
        passwordRecoveryScreen(navController)
        signInOtpScreen(navController)
        phoneNumberConfirmationScreen(navController)
    }
}

fun NavHostController.navigateToSignInGraph() {
    this.navigate(SignInGraph.route)
}
