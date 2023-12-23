package ru.zarina.zarina.ui.bottombar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.navigation.rework.destination.BaseRouteReworked

@Stable
sealed class ZarinaBottomNavBarItem(
    @StringRes
    val titleResId: Int,
    @DrawableRes
    val iconResId: Int,
    val baseRoute: BaseRouteReworked,
) {
    data object Catalog : ZarinaBottomNavBarItem(
        titleResId = R.string.catalog,
        iconResId = R.drawable.ic_catalog_24,
        baseRoute = BaseRouteReworked.CATALOG_GRAPH,
    )

    data object Favorites : ZarinaBottomNavBarItem(
        titleResId = R.string.favorites,
        iconResId = R.drawable.ic_heart_outline_24,
        baseRoute = BaseRouteReworked.FAVORITES_GRAPH,
    )

    data object Home : ZarinaBottomNavBarItem(
        titleResId = R.string.home,
        iconResId = R.drawable.ic_home_outline_24,
        baseRoute = BaseRouteReworked.HOME_GRAPH,
    )

    data object Profile : ZarinaBottomNavBarItem(
        titleResId = R.string.profile,
        iconResId = R.drawable.ic_profile_outline_24,
        baseRoute = BaseRouteReworked.PROFILE_GRAPH,
    )

    data object Cart : ZarinaBottomNavBarItem(
        titleResId = R.string.cart,
        iconResId = R.drawable.ic_cart_outline_24,
        baseRoute = BaseRouteReworked.CART_GRAPH,
    )
}
