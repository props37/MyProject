package ru.zarina.zarina.ui.navigation.rework.destination

enum class BaseRouteReworked {
    ONBOARDING;

    val route: String get() = name.lowercase()
}
