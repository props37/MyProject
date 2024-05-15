package ru.livetyping.zarina.presentation.navigation.base.parameterless

import androidx.navigation.NamedNavArgument
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination

/**
 * An abstraction for a Compose Navigation destination that encapsulates destination's
 * [routeSchema], [arguments] and [deepLinks].
 *
 * Parameterless version of [Destination].
 *
 * @see [Destination].
 */
abstract class SimpleDestination(override val routeSchema: String) : Destination<Unit>() {

    constructor(baseRoute: BaseRoute) : this(baseRoute.route)

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
