package ru.livetyping.zarina.presentation.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.navigation.base.dialogDestination
import ru.livetyping.zarina.presentation.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation.SignOutConfirmationDialogScreen
import ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation.SignOutConfirmationScreenAction

fun NavGraphBuilder.signOutConfirmationDialog(navController: NavHostController) {
    dialogDestination(
        destination = ProfileGraph.SignOutConfirmation,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        SignOutConfirmationDialogScreen(
            navigate = { action ->
                when (action) {
                    SignOutConfirmationScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.SignOutConfirmation.routeSchema,
                            inclusive = true,
                        )
                    }

                    SignOutConfirmationScreenAction.UserSignedOut -> {
                        navController.popBackStack(
                            route = ProfileGraph.Profile.routeSchema,
                            inclusive = false,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToSignOutConfirmationDialog() {
    this.navigate(ProfileGraph.SignOutConfirmation.route)
}
