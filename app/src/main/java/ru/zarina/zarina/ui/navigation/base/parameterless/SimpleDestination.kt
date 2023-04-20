package ru.zarina.zarina.ui.navigation.base.parameterless

import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.destinations.BaseRoute

@Suppress("Unused")
open class SimpleDestination(baseRoute: BaseRoute) : Destination<Unit>() {

    override val routeSchema: String = baseRoute.name

    override fun createRoute(args: Unit) = routeSchema

    val route: String
        get() = createRoute(Unit)

}
