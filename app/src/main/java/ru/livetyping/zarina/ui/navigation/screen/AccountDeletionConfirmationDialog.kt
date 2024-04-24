package ru.livetyping.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.dialogDestination
import ru.livetyping.zarina.ui.navigation.destination.graph.ProfileGraph
import ru.livetyping.zarina.ui.screen.profile.details.accountdeletionconfirmation.AccountDeletionConfirmationDialogScreen
import ru.livetyping.zarina.ui.screen.profile.details.accountdeletionconfirmation.AccountDeletionConfirmationScreenAction

fun NavGraphBuilder.accountDeletionConfirmationDialog(navController: NavHostController) {
    dialogDestination(
        destination = ProfileGraph.AccountDeletionConfirmation,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        AccountDeletionConfirmationDialogScreen(
            navigate = { action ->
                when (action) {
                    AccountDeletionConfirmationScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = ProfileGraph.AccountDeletionConfirmation.routeSchema,
                            inclusive = true,
                        )
                    }

                    AccountDeletionConfirmationScreenAction.AccountDeleted -> {
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

fun NavHostController.navigateToAccountDeletionConfirmationDialog() {
    this.navigate(ProfileGraph.AccountDeletionConfirmation.route)
}
