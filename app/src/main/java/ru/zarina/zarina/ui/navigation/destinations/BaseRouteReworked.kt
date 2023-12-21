package ru.zarina.zarina.ui.navigation.destinations

enum class BaseRouteReworked {
    ONBOARDING;

    val route: String get() = name.lowercase()
}
