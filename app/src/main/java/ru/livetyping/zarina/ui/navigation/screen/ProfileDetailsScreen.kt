package ru.livetyping.zarina.ui.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.composableDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.navigation.util.slideEnterTransition
import ru.livetyping.zarina.ui.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.ui.screen.profile.details.ProfileDetailsScreen
import ru.livetyping.zarina.ui.screen.profile.details.ProfileDetailsScreenAction

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
