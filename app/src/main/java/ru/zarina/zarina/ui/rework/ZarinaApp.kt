package ru.zarina.zarina.ui.rework

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import ru.zarina.zarina.ui.bottomnavbar.ZarinaBottomNavBar
import ru.zarina.zarina.ui.navigation.rework.ZarinaNavigation
import ru.zarina.zarina.util.library.accompanist.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaApp(
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    Box(modifier = modifier) {
        ZarinaNavigation(
            navController = navController,
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
