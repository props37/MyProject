package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.signin.otp.SignInOtpScreen
import ru.livetyping.zarina.presentation.screen.signin.otp.SignInOtpScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.signInOtpScreen(navController: NavHostController) {
    composableDestination(
        destination = SignInGraph.Otp,
        enterTransition = {
            when (initialState.destination.route) {
                SignInGraph.SignIn.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                SignInGraph.SignIn.routeSchema,
                ProfileGraph.Profile.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        SignInOtpScreen(
            navigate = { action ->
                when (action) {
                    SignInOtpScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SignInGraph.Otp.routeSchema,
                            inclusive = true,
                        )
                    }

                    SignInOtpScreenAction.UserSignedIn -> {
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

fun NavHostController.navigateToSignInOtpScreen(phone: PhoneNumber) {
    val args = SignInGraph.Otp.Args(phone)
    this.navigate(
        route = SignInGraph.Otp.routeSchema,
        args = SignInGraph.Otp.createArgsBundle(args),
    )
}
