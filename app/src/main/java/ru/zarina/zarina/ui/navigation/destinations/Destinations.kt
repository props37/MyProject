package ru.zarina.zarina.ui.navigation.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Filtration
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.base.OptionalNavArg
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.screens.catalog.filters.FilterType

object Destinations {
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

object Home : SimpleGraph(BaseRoute.GRAPH_HOME, Root) {
    object Root : SimpleDestination(BaseRoute.HOME)
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
        const val ARGUMENT_FILTRATION = "filtration"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.CATALOG_PRODUCTS,
            argNames = arrayOf(ARGUMENT_CATEGORY_ID),
            optionalArgNames = arrayOf(ARGUMENT_FILTRATION)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_CATEGORY_ID) { type = NavType.IntType },
            navArgument(ARGUMENT_FILTRATION) {
                type = NavType.Filtration
                nullable = true
            }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.CATALOG_PRODUCTS,
            args = arrayOf(args.categoryId.value),
            optionalArgs = arrayOf(OptionalNavArg(ARGUMENT_FILTRATION, args.filtration))
        )

        data class Arguments(
            val categoryId: Category.Id,
            val filtration: Filtration?,
        )
    }

    object SelectSort : SimpleDestination(BaseRoute.CATALOG_SELECT_SORT)

    object Filters : SimpleDestination(BaseRoute.CATALOG_FILTERS)

    object ListFilter : Destination<ListFilter.Arguments>() {

        const val ARGUMENT_FILTER_TYPE = "filter_type"

        override val routeSchema = RouteUtils.generateRouteSchema(
            baseRoute = BaseRoute.CATALOG_LIST_FILTER,
            argNames = arrayOf(ARGUMENT_FILTER_TYPE)
        )

        override val arguments = listOf(
            navArgument(ARGUMENT_FILTER_TYPE) { type = NavType.EnumType(FilterType::class.java) }
        )

        override fun createRoute(args: Arguments) = RouteUtils.generateRoute(
            baseRoute = BaseRoute.CATALOG_LIST_FILTER,
            args = arrayOf(args.filterType)
        )

        data class Arguments(
            val filterType: FilterType,
        )
    }

    object SelectPickupShop : SimpleDestination(BaseRoute.CATALOG_SELECT_PICKUP_SHOP)

    object SelectCity : SimpleDestination(BaseRoute.CATALOG_SELECT_CITY)

}

object Favorites : SimpleGraph(BaseRoute.GRAPH_FAVOURITES, Root) {
    object Root : SimpleDestination(BaseRoute.FAVOURITES)
}

object Profile : SimpleGraph(BaseRoute.GRAPH_PROFILE, Root) {
    object Root : SimpleDestination(BaseRoute.PROFILE)
}

object Cart : SimpleGraph(BaseRoute.GRAPH_CART, Root) {
    object Root : SimpleDestination(BaseRoute.CART)
}
