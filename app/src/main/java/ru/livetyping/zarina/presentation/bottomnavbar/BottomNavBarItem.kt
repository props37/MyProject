package ru.livetyping.zarina.presentation.bottomnavbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import ru.livetyping.zarina.R
import ru.livetyping.zarina.core.feature.NavigationEntry
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.core.resource.R as RCommon

@Stable
sealed class BottomNavBarItem(
    @StringRes
    val titleResId: Int,
    @DrawableRes
    val iconResId: Int,
) {
    data object Catalog : BottomNavBarItem(
        titleResId = RCommon.string.res_catalog,
        iconResId = R.drawable.ic_menu_24,
    )

    data object Wishlist : BottomNavBarItem(
        titleResId = RCommon.string.res_wishlist,
        iconResId = R.drawable.ic_heart_outline_24,
    )

    data object Home : BottomNavBarItem(
        titleResId = RCommon.string.res_home_screen,
        iconResId = R.drawable.ic_building_outline_24,
    )

    data object Profile : BottomNavBarItem(
        titleResId = RCommon.string.res_profile,
        iconResId = R.drawable.ic_human_outline_24,
    )

    data object Cart : BottomNavBarItem(
        titleResId = RCommon.string.res_cart,
        iconResId = R.drawable.ic_shopper_outline_24,
    )
}

fun BottomNavBarItem.toFeatureNavEntry(): NavigationEntry {
    return when (this) {
        BottomNavBarItem.Catalog -> CatalogFeature.getNavEntry()
        BottomNavBarItem.Wishlist -> WishlistFeature.getNavEntry()
        BottomNavBarItem.Home -> HomeFeature.getNavEntry()
        BottomNavBarItem.Profile -> ProfileFeature.getNavEntry()
        BottomNavBarItem.Cart -> CartFeature.getNavEntry()
    }
}

// Source: https://developer.android.com/jetpack/compose/navigation#bottom-nav
fun NavHostController.navigateToBottomNavBarItem(item: BottomNavBarItem) {
    val navEntry = item.toFeatureNavEntry()
    this.navigate(navEntry) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(HomeFeature.getNavEntry()) { saveState = true }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

val BottomNavBarItems: List<BottomNavBarItem> = listOf(
    BottomNavBarItem.Catalog,
    BottomNavBarItem.Wishlist,
    BottomNavBarItem.Home,
    BottomNavBarItem.Profile,
    BottomNavBarItem.Cart,
)
