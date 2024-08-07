package ru.livetyping.zarina.presentation.navigation.destination.graph

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.Graph
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils

data object OrderPlacementGraph : Graph<OrderPlacementGraph.Recipient.Args>() {

    private val routeBase: String
        get() = BaseRoute.ORDER_PLACEMENT_GRAPH.route

    override val routeSchema: String
        get() = RouteUtils.generateRouteSchema(
            routeBase = routeBase,
            argNames = arrayOf(Recipient.ARG_KEY_CART_TYPE),
        )

    override fun createRoute(args: Recipient.Args): String {
        val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
        return RouteUtils.generateRoute(
            routeBase = routeBase,
            args = arrayOf(cartTypeParcelable),
        )
    }

    override val arguments: List<NamedNavArgument>
        get() = Recipient.arguments

    override fun createArgsBundle(args: Recipient.Args): Bundle {
        return Recipient.createArgsBundle(args)
    }

    override val startDestination = Recipient



    data object Recipient : Destination<Recipient.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"

        private val routeBase: String
            get() = BaseRoute.RECIPIENT.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
        }

        data class Args(val cartType: CartType)
    }
}
