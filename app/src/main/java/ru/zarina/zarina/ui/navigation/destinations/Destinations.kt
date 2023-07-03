package ru.zarina.zarina.ui.navigation.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

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

    object Webpage : Destination<Webpage.Arguments>() {

        const val ARGUMENT_URL = "url"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.WEBPAGE,
            argNames = arrayOf(ARGUMENT_URL)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_URL) { type = NavType.StringType }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.WEBPAGE,
            args = arrayOf(args.url)
        )

        data class Arguments(
            val url: String,
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


object Subscribe : Graph<Subscribe.Arguments>() {

    const val ARGUMENT_OFFER_BARCODE = "offer_barcode"

    override val routeSchema = RouteUtils.generateRouteSchema(
        baseRoute = BaseRoute.GRAPH_SUBSCRIBE,
        argNames = arrayOf(ARGUMENT_OFFER_BARCODE)
    )
    override val startDestination = Root

    override val arguments = listOf(
        navArgument(ARGUMENT_OFFER_BARCODE) { type = NavType.StringType }
    )

    override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
        baseRoute = BaseRoute.GRAPH_SUBSCRIBE,
        args = arrayOf(args.offerBarcode.value)
    )

    data class Arguments(
        val offerBarcode: Barcode,
    )

    object Root : SimpleDestination(BaseRoute.SUBSCRIBE)

    object Success : Destination<Success.Arguments>() {

        const val ARGUMENT_EMAIL = "email"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.SUBSCRIBE_SUCCESS,
            argNames = arrayOf(ARGUMENT_EMAIL)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_EMAIL) { type = NavType.StringType }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.SUBSCRIBE_SUCCESS,
            args = arrayOf(args.email)
        )

        data class Arguments(
            val email: String,
        )
    }

}

object Catalog : SimpleGraph(BaseRoute.GRAPH_CATALOG, Categories) {

    object Categories : SimpleDestination(BaseRoute.CATALOG_CATEGORIES)

    object Products : Destination<Products.Arguments>() {

        const val ARGUMENT_CATEGORY_ID = "category_id"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.CATALOG_PRODUCTS,
            argNames = arrayOf(ARGUMENT_CATEGORY_ID)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_CATEGORY_ID) { type = NavType.IntType }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.CATALOG_PRODUCTS,
            args = arrayOf(args.categoryId.value)
        )

        data class Arguments(
            val categoryId: Category.Id,
        )
    }

    object SelectSort : SimpleDestination(BaseRoute.CATALOG_SELECT_SORT)

    object Filters : SimpleDestination(BaseRoute.CATALOG_FILTERS)

}
