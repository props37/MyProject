package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.profile.details.changeemail.ChangeEmailScreen
import ru.livetyping.zarina.presentation.screen.profile.details.changeemail.ChangeEmailScreenAction

fun NavGraphBuilder.changeEmailScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.ChangeEmail,
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
        ChangeEmailScreen(
            navigate = { action ->
                when (action) {
                    ChangeEmailScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangeEmail.routeSchema,
                            inclusive = true,
                        )
                    }

                    ChangeEmailScreenAction.EmailChanged -> {
                        navController.popBackStack(
                            route = ProfileGraph.ChangeEmail.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToChangeEmailScreen() {
    this.navigate(ProfileGraph.ChangeEmail.route)
}
