package ru.livetyping.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.dialogDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation.SignOutConfirmationDialogScreen
import ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation.SignOutConfirmationScreenAction

fun NavGraphBuilder.signOutConfirmationDialog(navController: NavHostController) {
    dialogDestination(
        destination = ProfileGraph.SignOutConfirmationDialog,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        SignOutConfirmationDialogScreen(
            navigate = { action ->
                when (action) {
                    SignOutConfirmationScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.SignOutConfirmationDialog.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            },
        )
    }
}

fun NavHostController.navigateToSignOutConfirmationDialog() {
    this.navigate(ProfileGraph.SignOutConfirmationDialog.route)
}
