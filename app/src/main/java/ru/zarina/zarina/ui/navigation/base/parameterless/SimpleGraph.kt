package ru.zarina.zarina.ui.navigation.base.parameterless

import androidx.navigation.NamedNavArgument
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.old.destinations.BaseRoute
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked

/**
 * An abstraction for a Compose Navigation nested graph that encapsulates graph's
 * [routeSchema] and [startDestination].
 *
 * Parameterless version of [Graph].
 *
 * @see [Graph].
 */
abstract class SimpleGraph(
    override val routeSchema: String,
    override val startDestination: Destination<*>,
) : Graph<Unit>() {

    constructor(
        baseRoute: BaseRoute,
        startDestination: Destination<*>,
    ) : this(baseRoute.route, startDestination)

    constructor(
        baseRoute: BaseRouteReworked,
        startDestination: Destination<*>,
    ) : this(baseRoute.route, startDestination)

    /**
     * String that can be used as a route for NavController navigation.
     */
    val route: String get() = routeSchema

    @Deprecated(
        message = "Use 'route' property instead",
        replaceWith = ReplaceWith("route"),
        level = DeprecationLevel.WARNING,
    )
    override fun createRoute(args: Unit): String = route

    final override val arguments: List<NamedNavArgument>
        get() = super.arguments
}
