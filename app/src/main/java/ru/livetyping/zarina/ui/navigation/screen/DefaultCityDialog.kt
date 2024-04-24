package ru.livetyping.zarina.ui.navigation.screen

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.navigation.base.dialogDestination
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.screen.onboarding.defaultcity.DefaultCityDialogScreen
import ru.livetyping.zarina.ui.screen.onboarding.defaultcity.DefaultCityScreenAction

fun NavGraphBuilder.defaultCityDialog(navController: NavHostController) {
    dialogDestination(
        destination = UnscopedDestinations.DefaultCity,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        DefaultCityDialogScreen(
            navigate = { action ->
                when (action) {
                    DefaultCityScreenAction.ScreenClosed -> {
                        navController.popBackStack(
                            route = UnscopedDestinations.DefaultCity.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}

fun NavHostController.navigateToDefaultCityDialog() {
    this.navigate(UnscopedDestinations.DefaultCity.route)
}
