package ru.zarina.zarina.ui.navigation.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination

object Destinations {
    object Home : SimpleDestination(BaseRoute.HOME)

    object Onboarding : SimpleDestination(BaseRoute.ONBOARDING)

    object SelectCity : SimpleDestination(BaseRoute.SELECT_CITY)

    object Product : Destination<Product.Arguments>() {

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
            val productId: ru.zarina.zarina.domain.Product.Id,
        )
    }
}


object Pickup : Graph<Pickup.Arguments>() {

    const val ARGUMENT_PRODUCT_ID = "product_id"

    override val routeSchema = RouteUtils.generateRouteSchema(
        baseRoute = BaseRoute.GRAPH_PICKUP,
        argNames = arrayOf(ARGUMENT_PRODUCT_ID)
    )
    override val startDestination = Root

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

    object Root : Destination<Root.Arguments>() {

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

    object SelectSize : SimpleDestination(BaseRoute.SELECT_SIZE)

    object SelectCity : SimpleDestination(BaseRoute.SELECT_PICKUP_CITY)

    object Details : SimpleDestination(BaseRoute.PICKUP_DETAILS)

    object Success : SimpleDestination(BaseRoute.PICKUP_SUCCESS)

}
