package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp.ChangePhoneNumberOtpScreen
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp.ChangePhoneNumberOtpScreenAction
import ru.livetyping.zarina.util.library.navigation.navigate

fun NavGraphBuilder.changePhoneNumberOtpScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.ChangePhoneNumberOtp,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.ChangePhoneNumber.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.ChangePhoneNumber.routeSchema,
                ProfileGraph.ProfileDetails.routeSchema -> slidePopExitTransition()

                else -> null
            }
        },
    ) {
        ChangePhoneNumberOtpScreen(
            navigate = { action ->
                when (action) {
                    ChangePhoneNumberOtpScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangePhoneNumberOtp.routeSchema,
                            inclusive = true,
                        )
                    }

                    ChangePhoneNumberOtpScreenAction.PhoneNumberChanged -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangePhoneNumber.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToChangePhoneNumberOtpScreen(phone: PhoneNumber) {
    val args = ProfileGraph.ChangePhoneNumberOtp.Args(phone)
    this.navigate(
        route = ProfileGraph.ChangePhoneNumberOtp.routeSchema,
        args = ProfileGraph.ChangePhoneNumberOtp.createArgsBundle(args),
    )
}
