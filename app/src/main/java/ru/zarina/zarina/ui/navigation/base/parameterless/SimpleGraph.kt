package ru.zarina.zarina.ui.navigation.base.parameterless

import androidx.navigation.NamedNavArgument
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.destinations.BaseRoute

@Suppress("Unused")
open class SimpleGraph(
    baseRoute: BaseRoute,
    override val startDestination: Destination<*>,
) : Graph<Unit>() {

    override val routeSchema: String = baseRoute.name

    override fun createRoute(args: Unit): String = routeSchema

    final override val arguments: List<NamedNavArgument>
        get() = super.arguments

}
