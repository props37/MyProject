package ru.livetyping.zarina.presentation.navigation.screen.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import ru.livetyping.zarina.presentation.navigation.base.navigationGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.presentation.navigation.screen.signUpOtpScreen
import ru.livetyping.zarina.presentation.navigation.screen.signUpScreen
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.signUpGraph(navController: NavHostController) {
    navigationGraph(SignUpGraph) {
        signUpScreen(navController)
        signUpOtpScreen(navController)
    }
}

fun NavHostController.navigateToSignUpGraph(
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = SignUpGraph.route,
        args = null,
        navOptions = navOptions,
    )
}
