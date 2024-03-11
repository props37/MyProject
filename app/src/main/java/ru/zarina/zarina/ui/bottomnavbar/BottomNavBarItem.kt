package ru.zarina.zarina.ui.bottomnavbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.destination.graph.HomeGraph

@Stable
sealed class BottomNavBarItem(
    @StringRes
    val titleResId: Int,
    @DrawableRes
    val iconResId: Int,
    val baseRoute: BaseRoute,
) {
    data object Catalog : BottomNavBarItem(
        titleResId = R.string.catalog,
        iconResId = R.drawable.ic_catalog_24,
        baseRoute = BaseRoute.CATALOG_GRAPH,
    )

    data object Favorites : BottomNavBarItem(
        titleResId = R.string.favorites,
        iconResId = R.drawable.ic_heart_outline_24,
        baseRoute = BaseRoute.FAVORITES_GRAPH,
    )

    data object Home : BottomNavBarItem(
        titleResId = R.string.home,
        iconResId = R.drawable.ic_home_outline_24,
        baseRoute = BaseRoute.HOME_GRAPH,
    )

    data object Profile : BottomNavBarItem(
        titleResId = R.string.profile,
        iconResId = R.drawable.ic_profile_outline_24,
        baseRoute = BaseRoute.PROFILE_GRAPH,
    )

    data object Cart : BottomNavBarItem(
        titleResId = R.string.cart,
        iconResId = R.drawable.ic_cart_outline_24,
        baseRoute = BaseRoute.CART_GRAPH,
    )

    companion object {
        val ITEMS: List<BottomNavBarItem>
            get() = listOf(Catalog, Favorites, Home, Profile, Cart)
    }
}

// Source: https://developer.android.com/jetpack/compose/navigation#bottom-nav
fun NavHostController.navigateToBottomNavBarItem(item: BottomNavBarItem) {
    this.navigate(item.baseRoute.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(HomeGraph.Home.routeSchema) { saveState = true }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}
