package ru.livetyping.zarina.ui.navigation.destination.graph

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.ui.model.product.ProductItemParcelable
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable
import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.Destination
import ru.livetyping.zarina.ui.navigation.base.Graph
import ru.livetyping.zarina.ui.navigation.base.RouteUtils
import ru.livetyping.zarina.ui.navigation.base.ScreenResult
import ru.livetyping.zarina.ui.navigation.navtype.ProductOfferParcelableArrayType
import ru.livetyping.zarina.ui.navigation.navtype.ProductParcelableType
import java.util.UUID

data object SizeSelectorGraph : Graph<SizeSelectorGraph.SizeSelector.Args>() {
    const val RESULT_KEY = "result_size_selector"

    private val routeBase: String
        get() = BaseRoute.SIZE_SELECTOR_GRAPH.route

    override val routeSchema: String
        get() = RouteUtils.generateRouteSchema(
            routeBase = routeBase,
            argNames = arrayOf(SizeSelector.ARG_KEY_PRODUCT),
        )

    override fun createRoute(args: SizeSelector.Args): String {
        val productParcelable = ProductItemParcelable.from(args.product)
        val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
        return RouteUtils.generateRoute(
            routeBase = routeBase,
            args = arrayOf(productParcelableString),
        )
    }

    override val startDestination: Destination<*> get() = SizeSelector

    override fun createArgsBundle(args: SizeSelector.Args): Bundle {
        return SizeSelector.createArgsBundle(args)
    }

    @Parcelize
    data class Result(
        val product: ProductItemParcelable,
        val offer: ProductOfferParcelable,
        override val id: String = UUID.randomUUID().toString(),
    ) : ScreenResult, Parcelable


    data object SizeSelector : Destination<SizeSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"

        private val baseRoute: String
            get() = BaseRoute.SIZE_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductItemParcelable.from(args.product)
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

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val productParcelable = ProductItemParcelable.from(args.product)
            putParcelable(ARG_KEY_PRODUCT, productParcelable)
        }

        data class Args(val product: Product)
    }

    data object HeightSelector : Destination<HeightSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"
        const val ARG_KEY_OFFERS = "arg_offers"

        private val baseRoute: String
            get() = BaseRoute.HEIGHT_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT, ARG_KEY_OFFERS),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductItemParcelable.from(args.product)
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

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val productParcelable = ProductItemParcelable.from(args.product)
            putParcelable(ARG_KEY_PRODUCT, productParcelable)
            val offerParcelables = args.offers.map { ProductOfferParcelable.from(it) }
            putParcelableArray(ARG_KEY_OFFERS, offerParcelables.toTypedArray())
        }

        data class Args(val product: Product, val offers: List<ProductOffer>)
    }
}
