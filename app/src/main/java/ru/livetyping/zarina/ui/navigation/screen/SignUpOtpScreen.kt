package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.signup.otp.SignUpOtpScreen
import ru.livetyping.zarina.ui.screen.signup.otp.SignUpOtpScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.signUpOtpScreen(navController: NavHostController) {
    composableDestination(
        destination = SignUpGraph.Otp,
        enterTransition = {
            when (initialState.destination.route) {
                SignUpGraph.SignUp.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                SignUpGraph.SignUp.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        SignUpOtpScreen(
            navigate = { action ->
                when (action) {
                    SignUpOtpScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = SignUpGraph.Otp.routeSchema,
                            inclusive = true,
                        )
                    }

                    SignUpOtpScreenAction.SignUpConfirmed -> {
                        navController.popBackStack(
                            route = SignUpGraph.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToSignUpOtpScreen(phone: PhoneNumber) {
    val args = SignUpGraph.Otp.Args(phone)
    this.navigate(
        route = SignUpGraph.Otp.routeSchema,
        args = SignUpGraph.Otp.createArgsBundle(args),
    )
}
