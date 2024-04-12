package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryScreen
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryScreenAction

fun NavGraphBuilder.passwordRecoveryScreen(navController: NavHostController) {
    composableDestination(
        destination = SignInGraph.PasswordRecovery,
        enterTransition = {
            when (initialState.destination.route) {
                SignInGraph.SignIn.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                SignInGraph.SignIn.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        PasswordRecoveryScreen(
            navigate = { action ->
                when (action) {
                    PasswordRecoveryScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SignInGraph.PasswordRecovery.routeSchema,
                            inclusive = true,
                        )
                    }

                    PasswordRecoveryScreenAction.PasswordResetRequested -> {
                        navController.popBackStack(
                            route = SignInGraph.PasswordRecovery.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToPasswordRecoveryScreen() {
    this.navigate(SignInGraph.PasswordRecovery.route)
}
