package ru.zarina.zarina.ui.navigation.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination

object Destinations {
    val HOME = SimpleDestination(BaseRoute.HOME)
    val ONBOARDING = SimpleDestination(BaseRoute.ONBOARDING)
    val CITY_SELECTION = SimpleDestination(BaseRoute.CITY_SELECTION)
    val PRODUCT = ProductDestination
}

object ProductDestination : Destination<String>() {

    private const val ARGUMENT_PRODUCT_ID = "product_id"

    override val routeSchema = RouteUtils.generateRouteSchema(
        baseRoute = BaseRoute.PRODUCT,
        argNames = arrayOf(ARGUMENT_PRODUCT_ID)
    )

    override val arguments = listOf(
        navArgument(ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
    )

    override fun createRoute(args: String) = RouteUtils.generateRoute(
        baseRoute = BaseRoute.PRODUCT,
        args = arrayOf(args)
    )

}
