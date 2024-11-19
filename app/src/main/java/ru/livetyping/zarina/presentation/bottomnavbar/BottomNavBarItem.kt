package ru.livetyping.zarina.presentation.bottomnavbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.presentation.navigation.destination.graph.HomeGraph
import ru.livetyping.zarina.core.resource.R as RCommon

@Stable
sealed class BottomNavBarItem(
    @StringRes
    val titleResId: Int,
    @DrawableRes
    val iconResId: Int,
) {
    data object Catalog : BottomNavBarItem(
        titleResId = R.string.catalog,
        iconResId = R.drawable.ic_menu_24,
    )

    data object Wishlist : BottomNavBarItem(
        titleResId = RCommon.string.wishlist,
        iconResId = R.drawable.ic_heart_outline_24,
    )

    data object Home : BottomNavBarItem(
        titleResId = R.string.home,
        iconResId = R.drawable.ic_building_outline_24,
    )

    data object Profile : BottomNavBarItem(
        titleResId = R.string.profile,
        iconResId = R.drawable.ic_human_outline_24,
    )

    data object Cart : BottomNavBarItem(
        titleResId = R.string.cart,
        iconResId = R.drawable.ic_shopper_outline_24,
    )

    companion object {
        val ITEMS: List<BottomNavBarItem> = listOf(Catalog, Wishlist, Home, Profile, Cart)
    }
}

fun BottomNavBarItem.toFeatureNavEntry(): Any {
    return when (this) {
        BottomNavBarItem.Catalog -> CatalogFeature.NavEntry
        BottomNavBarItem.Wishlist -> WishlistFeature.NavEntry
        BottomNavBarItem.Home -> HomeFeature.NavEntry
        BottomNavBarItem.Profile -> HomeFeature.NavEntry // TODO: [Top] Implement
        BottomNavBarItem.Cart -> HomeFeature.NavEntry // TODO: [Top] Implement
    }
}

// Source: https://developer.android.com/jetpack/compose/navigation#bottom-nav
fun NavHostController.navigateToBottomNavBarItem(item: BottomNavBarItem) {
    val navEntry = item.toFeatureNavEntry()
    this.navigate(navEntry) {
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
