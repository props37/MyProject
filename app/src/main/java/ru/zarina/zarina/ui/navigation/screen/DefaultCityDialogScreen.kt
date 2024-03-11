package ru.zarina.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.zarina.zarina.ui.navigation.base.dialogDestination
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreen
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreenAction

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
