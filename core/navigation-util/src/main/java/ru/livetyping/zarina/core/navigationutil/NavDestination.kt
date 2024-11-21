package ru.livetyping.zarina.core.navigationutil

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlin.reflect.KClass

public fun NavDestination.hasAnyRoute(routes: List<KClass<out Any>>): Boolean {
    for (route in routes) {
        if (this.hasRoute(route)) return true
    }
    return false
}

public fun NavDestination.withParent(includeTopMostParent: Boolean = false): NavDestinationWithParent {
    val parent = if (includeTopMostParent) {
        this.parent
    } else {
        this.parent?.takeIf { it.parent != null }
    }
    return NavDestinationWithParent(
        destination = this,
        parent = parent,
    )
}
