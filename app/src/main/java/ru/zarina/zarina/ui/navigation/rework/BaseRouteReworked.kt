package ru.zarina.zarina.ui.navigation.rework

enum class BaseRouteReworked {
    ONBOARDING,
    CITY_SELECTOR,
    DEFAULT_CITY_DIALOG,

    CATALOG_GRAPH,
    CATALOG,

    FAVORITES_GRAPH,
    FAVORITES,

    HOME_GRAPH,
    HOME,

    PROFILE_GRAPH,
    PROFILE,

    CART_GRAPH,
    CART;

    val route: String get() = name.lowercase()
}
