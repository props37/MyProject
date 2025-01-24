package ru.livetyping.zarina.presentation.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.navigation.util.slideEnterTransition
import ru.livetyping.zarina.presentation.navigation.util.slidePopExitTransition
import ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation.PhoneNumberConfirmationScreen
import ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation.PhoneNumberConfirmationScreenAction

fun NavGraphBuilder.phoneNumberConfirmationScreen(navController: NavHostController) {
    composable<SignInGraph.PhoneNumberConfirmation>(
        enterTransition = { slideEnterTransition() },
        popExitTransition = { slidePopExitTransition() },
    ) {
        PhoneNumberConfirmationScreen(
            navigate = { action ->
                when (action) {
                    PhoneNumberConfirmationScreenAction.ScreenClosed -> navController.navigateUp()
                    PhoneNumberConfirmationScreenAction.PhoneNumberConfirmed -> {
                        navController.popBackStack(
                            route = SignInGraph.routeSchema,
                            inclusive = true,
                        )
                    }
                }
            }
        )
    }
}
