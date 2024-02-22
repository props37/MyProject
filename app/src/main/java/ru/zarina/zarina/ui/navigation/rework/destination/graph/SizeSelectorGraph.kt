package ru.zarina.zarina.ui.navigation.rework.destination.graph

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.Graph
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductOfferParcelableArrayType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductParcelableType

data object SizeSelectorGraph : Graph<SizeSelectorGraph.SizeSelector.Args>() {
    const val RESULT_KEY = "result_size_selector"

    private val routeBase: String
        get() = BaseRouteReworked.SIZE_SELECTOR_GRAPH.route

    override val routeSchema: String
        get() = RouteUtils.generateRouteSchema(
            routeBase = routeBase,
            argNames = arrayOf(SizeSelector.ARG_KEY_PRODUCT),
        )

    override fun createRoute(args: SizeSelector.Args): String {
        val productParcelable = ProductParcelable.from(args.product)
        val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
        return RouteUtils.generateRoute(
            routeBase = routeBase,
            args = arrayOf(productParcelableString),
        )
    }

    override val startDestination: Destination<*> get() = SizeSelector

    @Parcelize
    data class Result(
        val id: String,
        val product: ProductParcelable,
        val offer: ProductOfferParcelable,
    ) : Parcelable


    data object SizeSelector : Destination<SizeSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"

        private val baseRoute: String
            get() = BaseRouteReworked.SIZE_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductParcelable.from(args.product)
            val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(productParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT) { type = NavType.ProductParcelableType }
            )

        data class Args(val product: Product)
    }

    data object HeightSelector : Destination<HeightSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"
        const val ARG_KEY_OFFERS = "arg_offers"

        private val baseRoute: String
            get() = BaseRouteReworked.HEIGHT_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT, ARG_KEY_OFFERS),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductParcelable.from(args.product)
            val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
            val offersParcelable = args.offers.map { ProductOfferParcelable.from(it) }
            val offersParcelableString = Uri.encode(Json.encodeToString(offersParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(productParcelableString, offersParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT) { type = NavType.ProductParcelableType },
                navArgument(ARG_KEY_OFFERS) { type = NavType.ProductOfferParcelableArrayType },
            )

        data class Args(val product: Product, val offers: List<ProductOffer>)
    }
}
