package ru.zarina.zarina.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.behavior.navigationbar.LocalNavigationBarController
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarBehavior
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.old.destinations.Catalog
import ru.zarina.zarina.ui.navigation.old.destinations.Favorites

private val BottomNavigationHeight = 56.dp

fun NavController.navigate(tab: BottomNavigationTab) {
    val isCurrentTab =
        currentDestination?.hierarchy?.any { it.route == tab.destination.routeSchema } == true
    val isCurrentScreen =
        currentDestination?.route == (tab.destination as? Graph<*>)?.startDestination?.routeSchema
    when {
        isCurrentTab && !isCurrentScreen -> {
            navigate(tab.destination.routeSchema) {
                popBackStack(tab.destination.routeSchema, true)
            }
        }

        !isCurrentTab -> {
            navigate(tab.destination.routeSchema) {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        else -> Unit
    }
}

sealed class BottomNavigationTab(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    val destination: Destination<*>,
) {

    object Catalogue : BottomNavigationTab(
        icon = R.drawable.old_ic_magnifying_glass_lines_36,
        title = R.string.catalogue,
        destination = Catalog,
    )

    object Favourites : BottomNavigationTab(
        icon = R.drawable.old_ic_heart_36,
        title = R.string.favorites,
        destination = Favorites,
    )

    object Home : BottomNavigationTab(
        icon = R.drawable.old_ic_home_36,
        title = R.string.main_page,
        destination = ru.zarina.zarina.ui.navigation.old.destinations.Home,
    )

    object Profile : BottomNavigationTab(
        icon = R.drawable.old_ic_person_36,
        title = R.string.profile,
        destination = ru.zarina.zarina.ui.navigation.old.destinations.Profile,
    )

    object Cart : BottomNavigationTab(
        icon = R.drawable.old_ic_shopping_bag_36,
        title = R.string.cart,
        destination = ru.zarina.zarina.ui.navigation.old.destinations.Cart,
    )


    companion object {
        val items = listOf(Catalogue, Favourites, Home, Profile, Cart)
    }
}

@Deprecated("Use bottomNavBarPadding instead.")
fun Modifier.bottomNavigationPadding(): Modifier = composed {
    this.padding(bottomNavigationPaddingValues())
}

@Composable
fun bottomNavigationPaddingValues(): PaddingValues {
    val behavior = LocalNavigationBarController.current.currentBehavior.collectAsStateWithLifecycle()
    val isVisible = remember { derivedStateOf { behavior.value is NavigationBarBehavior.Visible } }
    return PaddingValues(bottom = if (isVisible.value) BottomNavigationHeight else 0.dp)
}
