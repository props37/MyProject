package ru.zarina.zarina.ui.navigation.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination

object Destinations {
    val HOME = SimpleDestination(BaseRoute.HOME)
    val ONBOARDING = SimpleDestination(BaseRoute.ONBOARDING)
    val CITY_SELECTION = SimpleDestination(BaseRoute.CITY_SELECTION)
    val PRODUCT = ProductDestination
}

object ProductDestination : Destination<ProductDestination.Arguments>() {

    const val ARGUMENT_PRODUCT_ID = "product_id"

    override val routeSchema = RouteUtils.generateRouteSchema(
        baseRoute = BaseRoute.PRODUCT,
        argNames = arrayOf(ARGUMENT_PRODUCT_ID)
    )

    override val arguments = listOf(
        navArgument(ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
    )

    override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
        baseRoute = BaseRoute.PRODUCT,
        args = arrayOf(args.productId.value)
    )

    data class Arguments(
        val productId: Product.Id,
    )

}

object PickupGraph : Graph<PickupGraph.Arguments>() {

    const val ARGUMENT_PRODUCT_ID = "product_id"

    override val routeSchema = RouteUtils.generateRouteSchema(
        baseRoute = BaseRoute.GRAPH_PICKUP,
        argNames = arrayOf(ARGUMENT_PRODUCT_ID)
    )
    override val startDestination = PickupDestination

    override val arguments = listOf(
        navArgument(ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
    )

    override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
        baseRoute = BaseRoute.GRAPH_PICKUP,
        args = arrayOf(args.productId.value)
    )

    data class Arguments(
        val productId: Product.Id,
    )

    object PickupDestination : Destination<PickupDestination.Arguments>() {

        const val ARGUMENT_PRODUCT_ID = "product_id"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.PICKUP,
            argNames = arrayOf(ARGUMENT_PRODUCT_ID)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.PICKUP,
            args = arrayOf(args.productId.value)
        )

        data class Arguments(
            val productId: Product.Id,
        )

    }

    object SelectSizeDestination : Destination<SelectSizeDestination.Arguments>() {

        const val ARGUMENT_PRODUCT_ID = "product_id"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.SELECT_SIZE,
            argNames = arrayOf(ARGUMENT_PRODUCT_ID)
        )

        override val arguments = listOf(
            navArgument(PickupDestination.ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.SELECT_SIZE,
            args = arrayOf(args.productId.value)
        )

        data class Arguments(
            val productId: Product.Id,
        )

    }
}
