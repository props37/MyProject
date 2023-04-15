package ru.zarina.zarina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.destinations.BaseRoute
import ru.zarina.zarina.ui.screens.OnboardingScreen

@Composable
fun ZarinaNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = BaseRoute.ONBOARDING.name) {
        composableDestination(SimpleDestination(BaseRoute.HOME)) { }
        composableDestination(SimpleDestination(BaseRoute.ONBOARDING)) {
            OnboardingScreen(
                showHome = {
                    navController.navigate(BaseRoute.HOME.name) { popUpTo(0) }
                }
            )
        }
    }
}


