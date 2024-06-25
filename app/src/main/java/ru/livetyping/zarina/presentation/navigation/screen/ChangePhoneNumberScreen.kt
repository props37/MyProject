package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slideExitTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberScreen
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberScreenAction

fun NavGraphBuilder.changePhoneNumberScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.ChangePhoneNumber,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.ProfileDetails.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.ChangePhoneNumberOtp.routeSchema -> slideExitTransition()
                else -> null
            }
        },
        popEnterTransition = {
            when (targetState.destination.route) {
                ProfileGraph.ChangePhoneNumberOtp.routeSchema -> slideEnterTransition()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                ProfileGraph.ProfileDetails.routeSchema -> slidePopExitTransition()
                else -> null
            }
        },
    ) {
        ChangePhoneNumberScreen(
            navigate = { action ->
                when (action) {
                    ChangePhoneNumberScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangePhoneNumber.routeSchema,
                            inclusive = true,
                        )
                    }

                    is ChangePhoneNumberScreenAction.PhoneChangeRequested -> {
                        navController.navigateToChangePhoneNumberOtpScreen(action.phone)
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToChangePhoneNumberScreen() {
    this.navigate(ProfileGraph.ChangePhoneNumber.route)
}
