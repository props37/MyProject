package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slideExitTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.signup.SignUpScreen
import ru.livetyping.zarina.ui.screen.signup.SignUpScreenAction

fun NavGraphBuilder.signUpScreen(navController: NavHostController) {
    composableDestination(
        destination = SignUpGraph.SignUp,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                SignUpGraph.Otp.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                SignUpGraph.Otp.routeSchema -> slidePopEnterTransition()
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
        SignUpScreen(
            navigate = { action ->
                when (action) {
                    SignUpScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SignUpGraph.SignUp.routeSchema,
                            inclusive = true,
                        )
                    }

                    is SignUpScreenAction.UserCreated -> {
                        navController.navigateToSignUpOtpScreen(action.phone)
                    }
                }
            },
        )
    }
}
