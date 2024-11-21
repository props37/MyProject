package ru.livetyping.zarina.core.navigationutil

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph
import kotlin.reflect.KClass

public fun NavGraph.hasAnyRoute(routes: List<KClass<out Any>>): Boolean {
    for (route in routes) {
        if (this.hasRoute(route)) return true
    }
    return false
}
