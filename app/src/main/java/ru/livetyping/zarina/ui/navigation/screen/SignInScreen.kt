package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.ui.navigation.screen.graph.navigateToSignUpGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.signin.SignInScreen
import ru.livetyping.zarina.ui.screen.signin.SignInScreenAction

fun NavGraphBuilder.signInScreen(navController: NavHostController) {
    composableDestination(
        destination = SignInGraph.SignIn,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        SignInScreen(
            navigate = { action ->
                when (action) {
                    SignInScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SignInGraph.SignIn.routeSchema,
                            inclusive = true,
                        )
                    }

                    SignInScreenAction.SignUpClicked -> {
                        val navOptions = navOptions {
                            popUpTo(SignInGraph.routeSchema) { inclusive = true }
                        }
                        navController.navigateToSignUpGraph(navOptions)
                    }

                    SignInScreenAction.ForgotPasswordClicked -> {
                        navController.navigateToPasswordRecoveryScreen()
                    }

                    SignInScreenAction.UserSignedIn -> {
                        navController.popBackStack(
                            route = SignInGraph.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}
