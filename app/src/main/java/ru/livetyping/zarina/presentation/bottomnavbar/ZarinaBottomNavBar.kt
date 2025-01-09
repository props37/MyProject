package ru.livetyping.zarina.presentation.bottomnavbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import ru.livetyping.zarina.core.uikit.bottomnavbar.ZarinaBottomNavBar
import ru.livetyping.zarina.core.uikit.bottomnavbar.ZarinaBottomNavBarDefaults
import ru.livetyping.zarina.core.uikit.bottomnavbar.ZarinaBottomNavBarItem

@Composable
fun ZarinaBottomNavBar(
    navController: NavHostController,
    wishlistProductCountProvider: () -> Int,
    cartProductCountProvider: () -> Int,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = ZarinaBottomNavBarDefaults.DefaultWindowInsets,
) {
    ZarinaBottomNavBar(
        windowInsets = windowInsets,
        modifier = modifier,
    ) {
        // TODO: [Medium] Do not use restricted API
        val backStack by navController.currentBackStack.collectAsStateWithLifecycle()

        for (i in BottomNavBarItems.indices) {
            val item = BottomNavBarItems[i]
            key(item) {
                val counterValueProvider = when (item) {
                    BottomNavBarItem.Wishlist -> wishlistProductCountProvider
                    BottomNavBarItem.Cart -> cartProductCountProvider
                    else -> null
                }

                ZarinaBottomNavBarItem(
                    title = stringResource(item.titleResId),
                    iconResId = item.iconResId,
                    isSelected = isItemSelected(item, backStack),
                    onClick = { navController.navigateToBottomNavBarItem(item) },
                    counterValueProvider = counterValueProvider,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private fun isItemSelected(
    bottomNavItem: BottomNavBarItem,
    backStack: List<NavBackStackEntry>,
): Boolean {
    val bottomNavBarItemFeatureEntry = bottomNavItem.toFeatureNavEntry()
    val bottomNavBarItemNavEntries = BottomNavBarItems.map { it.toFeatureNavEntry() }
    val lastBottomNavBarItemBackStackEntry = backStack.lastOrNull { backStackEntry ->
        bottomNavBarItemNavEntries.any { navEntry ->
            backStackEntry.destination.hasRoute(navEntry::class)
        }
    }
    return lastBottomNavBarItemBackStackEntry?.destination
        ?.hasRoute(bottomNavBarItemFeatureEntry::class) ?: false
}
