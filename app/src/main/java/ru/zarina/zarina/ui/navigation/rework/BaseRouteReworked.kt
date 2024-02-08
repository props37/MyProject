package ru.zarina.zarina.ui.navigation.rework

enum class BaseRouteReworked {
    // Unscoped destinations
    ONBOARDING,
    CITY_SELECTOR,
    DEFAULT_CITY_DIALOG,

    // TODO: [High] Extract to nested graph
    PRODUCTS,
    FILTERS,
    LIST_FILTER,

    // TODO: [High] Extract to nested graph
    SIZE_SELECTOR,
    HEIGHT_SELECTOR,

    PRODUCT_SUBSCRIPTION,

    // Catalog graph
    CATALOG_GRAPH,
    CATALOG,

    // Favorites graph
    FAVORITES_GRAPH,
    FAVORITES,

    // Home graph
    HOME_GRAPH,
    HOME,

    // Profile graph
    PROFILE_GRAPH,
    PROFILE,

    // Cart graph
    CART_GRAPH,
    CART;

    val route: String get() = name.lowercase()
}
