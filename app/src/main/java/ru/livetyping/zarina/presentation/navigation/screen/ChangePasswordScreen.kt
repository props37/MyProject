package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordScreen
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordScreenAction

fun NavGraphBuilder.changePasswordScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.ChangePassword,
        enterTransition = {
            when (initialState.destination.route) {
                ProfileGraph.ProfileDetails.routeSchema -> slideEnterTransition()
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
        ChangePasswordScreen(
            navigate = { action ->
                when (action) {
                    ChangePasswordScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangePassword.routeSchema,
                            inclusive = true,
                        )
                    }

                    ChangePasswordScreenAction.PasswordChanged -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangePassword.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToChangePasswordScreen() {
    this.navigate(ProfileGraph.ChangePassword.route)
}
