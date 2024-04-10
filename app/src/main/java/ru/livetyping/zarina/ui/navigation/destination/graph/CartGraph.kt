package ru.livetyping.zarina.ui.navigation.destination.graph

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.model.cart.DeliveryTypeParcelable
import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.Destination
import ru.livetyping.zarina.ui.navigation.base.RouteUtils
import ru.livetyping.zarina.ui.navigation.base.ScreenResult
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph
import java.util.UUID

data object CartGraph : SimpleGraph(
    baseRoute = BaseRoute.CART_GRAPH,
    startDestination = Cart,
) {
    data object Cart : SimpleDestination(BaseRoute.CART)

    data object ProductCountSelector : Destination<ProductCountSelector.Args>() {
        const val ARG_KEY_PRODUCT_ID = "arg_product_id"
        const val ARG_KEY_BARCODE = "arg_barcode"
        const val ARG_KEY_INITIAL_COUNT = "arg_initial_count"
        const val ARG_KEY_AVAILABLE_COUNT = "arg_available_count"
        const val ARG_KEY_DELIVERY_TYPE = "arg_delivery_type"

        const val RESULT_KEY = "product_count_selector_result"

        private val routeBase: String
            get() = BaseRoute.PRODUCT_COUNT_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(
                    ARG_KEY_PRODUCT_ID,
                    ARG_KEY_BARCODE,
                    ARG_KEY_INITIAL_COUNT,
                    ARG_KEY_AVAILABLE_COUNT,
                    ARG_KEY_DELIVERY_TYPE,
                ),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(
                    args.productId.value,
                    args.barcode.value,
                    args.initialCount,
                    args.availableCount,
                    DeliveryTypeParcelable.from(args.deliveryType),
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT_ID) { type = NavType.StringType },
                navArgument(ARG_KEY_BARCODE) { type = NavType.StringType },
                navArgument(ARG_KEY_INITIAL_COUNT) { type = NavType.IntType },
                navArgument(ARG_KEY_AVAILABLE_COUNT) { type = NavType.IntType },
                navArgument(ARG_KEY_DELIVERY_TYPE) {
                    type = NavType.EnumType(DeliveryTypeParcelable::class.java)
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putString(ARG_KEY_PRODUCT_ID, args.productId.value)
            putString(ARG_KEY_BARCODE, args.barcode.value)
            putInt(ARG_KEY_INITIAL_COUNT, args.initialCount)
            putInt(ARG_KEY_AVAILABLE_COUNT, args.availableCount)
            putParcelable(ARG_KEY_DELIVERY_TYPE, DeliveryTypeParcelable.from(args.deliveryType))
        }

        data class Args(
            val productId: Product.Id,
            val barcode: Barcode,
            val initialCount: Int,
            val availableCount: Int,
            val deliveryType: DeliveryType,
        )

        @Parcelize
        data class Result(
            val countChanged: Boolean,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }
}
