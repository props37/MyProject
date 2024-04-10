package ru.livetyping.zarina.ui.navigation.util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ru.livetyping.zarina.ui.bottomnavbar.BottomNavBarItem
import ru.livetyping.zarina.ui.bottomnavbar.navigateToBottomNavBarItem

/**
 * Back handler that performs navigation to Home graph restoring its back stack state
 * rather than navigating to its start destination.
 */
@Composable
fun BottomNavBarItemSecondaryStartDestinationBackHandler(navController: NavHostController) {
    BackHandler {
        navController.navigateToBottomNavBarItem(BottomNavBarItem.Home)
    }
}
