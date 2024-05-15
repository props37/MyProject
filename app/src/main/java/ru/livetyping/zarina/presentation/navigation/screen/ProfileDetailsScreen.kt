package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.composableDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreen
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsScreenAction

fun NavGraphBuilder.profileDetailsScreen(navController: NavHostController) {
    composableDestination(
        destination = ProfileGraph.ProfileDetails,
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
        ProfileDetailsScreen(
            navigate = { action ->
                when (action) {
                    ProfileDetailsScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.ProfileDetails.routeSchema,
                            inclusive = true,
                        )
                    }

                    ProfileDetailsScreenAction.SignOutClicked -> {
                        navController.navigateToSignOutConfirmationDialog()
                    }

                    ProfileDetailsScreenAction.DeleteAccountClicked -> {
                        navController.navigateToAccountDeletionConfirmationDialog()
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToProfileDetailsScreen() {
    this.navigate(ProfileGraph.ProfileDetails.route)
}
