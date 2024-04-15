package ru.livetyping.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.dialogDestination
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.screen.onboarding.defaultcitydialog.DefaultCityDialogScreen
import ru.livetyping.zarina.ui.screen.onboarding.defaultcitydialog.DefaultCityDialogScreenAction

fun NavGraphBuilder.defaultCityDialogScreen(navController: NavHostController) {
    dialogDestination(
        destination = UnscopedDestinations.DefaultCityDialog,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        DefaultCityDialogScreen(
            navigate = { action ->
                when (action) {
                    DefaultCityDialogScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.DefaultCityDialog.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToDefaultCityDialog() {
    this.navigate(UnscopedDestinations.DefaultCityDialog.route)
}
