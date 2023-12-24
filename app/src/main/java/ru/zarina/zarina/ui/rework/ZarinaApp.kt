package ru.zarina.zarina.ui.rework

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import ru.zarina.zarina.ui.bottomnavbar.ZarinaBottomNavBar
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.rememberBottomNavBarBehaviorController
import ru.zarina.zarina.ui.navigation.rework.ZarinaNavigation
import ru.zarina.zarina.util.library.accompanist.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    val bottomNavBarBehaviorController = rememberBottomNavBarBehaviorController()

    CompositionLocalProvider(
        LocalBottomNavBarBehaviorController provides bottomNavBarBehaviorController,
    ) {
        Box(modifier = modifier) {
            ZarinaNavigation(
                navController = navController,
                startDestination = viewModel.startDestination,
                modifier = Modifier.fillMaxSize(),
            )

            ZarinaBottomNavBar(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            )
        }
    }
}
