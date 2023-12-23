package ru.zarina.zarina.ui.navigation.rework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import ru.zarina.zarina.ui.navigation.destinations.CommonDestinations
import ru.zarina.zarina.ui.navigation.rework.destination.onboardingScreen
import ru.zarina.zarina.util.library.accompanist.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaNavigation(
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    NavHost(
        navController = navController,
        startDestination = CommonDestinations.Onboarding.route,
        modifier = modifier,
    ) {
        onboardingScreen(navController)
    }
}
