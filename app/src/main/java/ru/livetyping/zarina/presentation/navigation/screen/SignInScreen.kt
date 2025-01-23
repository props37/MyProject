package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.screen.graph.navigateToSignUpGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.signin.SignInScreen
import ru.livetyping.zarina.presentation.screen.signin.SignInScreenAction

fun NavGraphBuilder.signInScreen(navController: NavHostController) {
    composableDestination(
        destination = SignInGraph.SignIn,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.Profile.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                SignInGraph.PasswordRecovery.routeSchema,
                SignInGraph.Otp.routeSchema -> slideExitTransition()

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                SignInGraph.PasswordRecovery.routeSchema,
                SignInGraph.Otp.routeSchema -> slidePopEnterTransition()

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

                    is SignInScreenAction.SignInByPhoneRequested -> {
                        navController.navigateToSignInOtpScreen(action.phone)
                    }

                    SignInScreenAction.UserSignedIn -> {
                        navController.popBackStack(
                            route = SignInGraph.routeSchema,
                            inclusive = true,
                        )
                    }

                    is SignInScreenAction.PhoneConfirmationNeeded -> {
                        TODO() // TODO: [Top] Implement
                    }
                }
            },
        )
    }
}
