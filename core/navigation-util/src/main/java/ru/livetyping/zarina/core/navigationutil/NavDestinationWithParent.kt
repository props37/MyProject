package ru.livetyping.zarina.core.navigationutil

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph
import kotlin.reflect.KClass

public data class NavDestinationWithParent(
    val destination: NavDestination,
    val parent: NavGraph?,
)

public inline fun <reified T : Any> NavDestinationWithParent.hasRoute(): Boolean {
    if (this.destination.hasRoute<T>()) return true
    return this.parent?.hasRoute<T>() == true
}

public fun NavDestinationWithParent.hasAnyRoute(routes: List<KClass<out Any>>): Boolean {
    if (this.destination.hasAnyRoute(routes)) return true
    return this.parent?.hasAnyRoute(routes) == true
}
