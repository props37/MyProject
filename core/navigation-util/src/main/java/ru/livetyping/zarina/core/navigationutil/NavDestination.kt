package ru.livetyping.zarina.core.navigationutil

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlin.reflect.KClass

public fun NavDestination.hasAnyRoute(routes: List<KClass<Any>>): Boolean {
    for (route in routes) {
        if (this.hasRoute(route)) return true
    }
    return false
}
