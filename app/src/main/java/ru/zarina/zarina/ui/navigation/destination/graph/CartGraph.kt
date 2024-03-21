package ru.zarina.zarina.ui.navigation.destination.graph

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object CartGraph : SimpleGraph(
    baseRoute = BaseRoute.CART_GRAPH,
    startDestination = Cart,
) {
    data object Cart : SimpleDestination(BaseRoute.CART)

    data object ProductCountSelector : Destination<ProductCountSelector.Args>() {
        const val ARG_KEY_PRODUCT_ID = "arg_product_id"
        const val ARG_KEY_BARCODE = "arg_barcode"
        const val ARG_KEY_AVAILABLE_COUNT = "arg_available_count"

        private val routeBase: String
            get() = BaseRoute.PRODUCT_COUNT_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                optionalArgNames = arrayOf(
                    ARG_KEY_PRODUCT_ID,
                    ARG_KEY_BARCODE,
                    ARG_KEY_AVAILABLE_COUNT,
                ),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf()
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT_ID) { type = NavType.StringType },
                navArgument(ARG_KEY_BARCODE) { type = NavType.StringType },
                navArgument(ARG_KEY_AVAILABLE_COUNT) { type = NavType.IntType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putString(ARG_KEY_PRODUCT_ID, args.productId.value)
            putString(ARG_KEY_BARCODE, args.barcode.value)
            putInt(ARG_KEY_AVAILABLE_COUNT, args.availableCount)
        }

        data class Args(
            val productId: Product.Id,
            val barcode: Barcode,
            val availableCount: Int,
        )
    }
}
