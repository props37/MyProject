package ru.livetyping.zarina.presentation.navigation

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
    PROFILE_DETAILS,
    CHANGE_PASSWORD,
    CHANGE_EMAIL,
    CHANGE_PHONE_NUMBER,
    CHANGE_PHONE_NUMBER_OTP,
    SIGN_OUT_CONFIRMATION,
    ACCOUNT_DELETION_CONFIRMATION,
    MY_ORDERS,
    ORDER,
    ORDER_CANCELLATION,
    STORES,

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

    // Loyalty card graph
    LOYALTY_PROGRAM_GRAPH,
    LOYALTY_PROGRAM,
    BONUS_HISTORY,

    // Order placement graph
    ORDER_PLACEMENT_GRAPH,
    RECIPIENT,
    STORE_SELECTION,

    // Unscoped destinations
    ONBOARDING,
    CITY_SELECTOR,
    DEFAULT_CITY,
    PRODUCTS,
    PRODUCT_FILTERS,
    PRODUCT_SEARCH,
    PRODUCT_SEARCH_FILTERS,
    PRODUCT,
    LIST_FILTER,
    PRODUCT_SUBSCRIPTION,
    PERMISSION_REQUIREMENT,

    // Generic
    GENERIC_BOTTOM_SHEET;

    val route: String get() = name.lowercase()
}
