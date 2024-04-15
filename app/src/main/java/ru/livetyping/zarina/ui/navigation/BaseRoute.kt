package ru.livetyping.zarina.ui.navigation

enum class BaseRoute {
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
    CART,
    PRODUCT_COUNT_SELECTOR,

    // Sign up graph
    SIGN_UP_GRAPH,
    SIGN_UP,
    SIGN_UP_OTP,

    // Sign in graph
    SIGN_IN_GRAPH,
    SIGN_IN,
    PASSWORD_RECOVERY,
    SIGN_IN_OTP,

    // Size selector graph
    SIZE_SELECTOR_GRAPH,
    SIZE_SELECTOR,
    HEIGHT_SELECTOR,

    // Unscoped destinations
    ONBOARDING,
    CITY_SELECTOR,
    DEFAULT_CITY_DIALOG,
    PRODUCTS,
    FILTERS,
    LIST_FILTER,
    PRODUCT_SUBSCRIPTION;

    val route: String get() = name.lowercase()
}
