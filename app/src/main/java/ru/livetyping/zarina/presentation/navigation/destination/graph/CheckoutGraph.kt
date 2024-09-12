package ru.livetyping.zarina.presentation.navigation.destination.graph

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.Graph
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils
import ru.livetyping.zarina.presentation.navigation.base.ScreenResult
import ru.livetyping.zarina.presentation.navigation.navtype.CartProductParcelableArrayType
import ru.livetyping.zarina.presentation.navigation.navtype.DeliveryDateTimePeriodParcelableArrayType
import ru.livetyping.zarina.presentation.navigation.navtype.StoreParcelableType
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CourierDeliveryDateTimeSelectorType
import java.util.UUID

data object CheckoutGraph : Graph<CheckoutGraph.Recipient.Args>() {

    private val routeBase: String
        get() = BaseRoute.CHECKOUT_GRAPH.route

    override val routeSchema: String
        get() = RouteUtils.generateRouteSchema(
            routeBase = routeBase,
            argNames = arrayOf(Recipient.ARG_KEY_CART_TYPE, Recipient.ARG_KEY_STEP),
        )

    override fun createRoute(args: Recipient.Args): String {
        val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
        return RouteUtils.generateRoute(
            routeBase = routeBase,
            args = arrayOf(cartTypeParcelable, args.step),
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
        const val ARG_KEY_STEP = "arg_step"

        private val routeBase: String
            get() = BaseRoute.RECIPIENT.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
        }

        data class Args(
            val cartType: CartType,
            val step: Int = 1,
        )
    }

    data object StoreSelection : Destination<StoreSelection.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"

        private val routeBase: String
            get() = BaseRoute.STORE_SELECTION.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
        )
    }

    data object SelectedStore : Destination<SelectedStore.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"
        const val ARG_KEY_STORE = "arg_store"
        const val ARG_KEY_AVAILABLE_PRODUCTS = "arg_available_products"

        private val routeBase: String
            get() = BaseRoute.SELECTED_STORE.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(
                    ARG_KEY_CART_TYPE,
                    ARG_KEY_STEP,
                    ARG_KEY_STORE,
                    ARG_KEY_AVAILABLE_PRODUCTS,
                ),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val storeParcelable = StoreParcelable.from(args.store)
            val storeParcelableString = Uri.encode(Json.encodeToString(storeParcelable))
            val productsParcelable = args.availableProducts.map {
                CartProductParcelable.from(it)
            }
            val productsParcelableString = Uri.encode(Json.encodeToString(productsParcelable))
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(
                    cartTypeParcelable,
                    args.step,
                    storeParcelableString,
                    productsParcelableString,
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
                navArgument(ARG_KEY_STORE) { type = NavType.StoreParcelableType },
                navArgument(ARG_KEY_AVAILABLE_PRODUCTS) {
                    type = NavType.CartProductParcelableArrayType
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val storeParcelable = StoreParcelable.from(args.store)
            val productsParcelable = args.availableProducts.map {
                CartProductParcelable.from(it)
            }
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
            putParcelable(ARG_KEY_STORE, storeParcelable)
            putParcelableArray(ARG_KEY_AVAILABLE_PRODUCTS, productsParcelable.toTypedArray())
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
            val store: Store,
            val availableProducts: List<CartProduct>,
        )
    }

    data object DeliveryMethod : Destination<DeliveryMethod.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"

        private val routeBase: String
            get() = BaseRoute.DELIVERY_METHOD.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
        )
    }

    data object CourierDelivery : Destination<CourierDelivery.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"
        const val ARG_DELIVERY_METHOD_TYPE = "arg_delivery_method_type"

        private val routeBase: String
            get() = BaseRoute.COURIER_DELIVERY.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP, ARG_DELIVERY_METHOD_TYPE),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryMethodTypeParcelable =
                DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step, deliveryMethodTypeParcelable),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
                navArgument(ARG_DELIVERY_METHOD_TYPE) {
                    type = NavType.EnumType(DeliveryMethodTypeParcelable::class.java)
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryTypeParcelable = DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
            putParcelable(ARG_DELIVERY_METHOD_TYPE, deliveryTypeParcelable)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
            val deliveryMethodType: DeliveryMethodType,
        )
    }

    data object CourierDeliveryDateTimeSelector :
        Destination<CourierDeliveryDateTimeSelector.Args>() {

        const val ARG_SELECTOR_TYPE = "arg_selector_type"
        const val ARG_DELIVERY_OPTION_ID = "arg_delivery_option_id"
        const val ARG_DATE_TIME_PERIODS = "arg_date_time_periods"

        const val RESULT_KEY = "courier_delivery_date_time_selector_result"

        private val routeBase: String
            get() = BaseRoute.COURIER_DELIVERY_DATE_TIME_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_SELECTOR_TYPE, ARG_DELIVERY_OPTION_ID, ARG_DATE_TIME_PERIODS),
            )

        override fun createRoute(args: Args): String {
            val dateTimePeriodsParcelable = args.dateTimePeriods.map {
                DeliveryDateTimePeriodParcelable.from(it)
            }
            val dateTimePeriodsParcelableString =
                Uri.encode(Json.encodeToString(dateTimePeriodsParcelable))
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(
                    args.type,
                    args.deliveryOptionId.value,
                    dateTimePeriodsParcelableString,
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_SELECTOR_TYPE) {
                    type = NavType.EnumType(CourierDeliveryDateTimeSelectorType::class.java)
                },
                navArgument(ARG_DELIVERY_OPTION_ID) { type = NavType.StringType },
                navArgument(ARG_DATE_TIME_PERIODS) {
                    type = NavType.DeliveryDateTimePeriodParcelableArrayType
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val dateTimePeriodsParcelable = args.dateTimePeriods.map {
                DeliveryDateTimePeriodParcelable.from(it)
            }
            putParcelable(ARG_SELECTOR_TYPE, args.type)
            putString(ARG_DELIVERY_OPTION_ID, args.deliveryOptionId.value)
            putParcelableArray(ARG_DATE_TIME_PERIODS, dateTimePeriodsParcelable.toTypedArray())
        }

        data class Args(
            val type: CourierDeliveryDateTimeSelectorType,
            val deliveryOptionId: DeliveryOptions.Option.Id,
            val dateTimePeriods: List<DeliveryOptions.Option.DateTimePeriod>,
        )

        @Parcelize
        data class Result(
            val deliveryOptionId: String,
            val selectorType: CourierDeliveryDateTimeSelectorType,
            val dateTimePeriod: DeliveryDateTimePeriodParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object PostDelivery : Destination<PostDelivery.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"
        const val ARG_DELIVERY_METHOD_TYPE = "arg_delivery_method_type"

        private val routeBase: String
            get() = BaseRoute.POST_DELIVERY.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP, ARG_DELIVERY_METHOD_TYPE),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryMethodTypeParcelable =
                DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step, deliveryMethodTypeParcelable),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
                navArgument(ARG_DELIVERY_METHOD_TYPE) {
                    type = NavType.EnumType(DeliveryMethodTypeParcelable::class.java)
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryTypeParcelable = DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
            putParcelable(ARG_DELIVERY_METHOD_TYPE, deliveryTypeParcelable)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
            val deliveryMethodType: DeliveryMethodType,
        )
    }

    data object PickupPointDelivery : Destination<PickupPointDelivery.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"
        const val ARG_DELIVERY_METHOD_TYPE = "arg_delivery_method_type"

        private val routeBase: String
            get() = BaseRoute.PICKUP_POINT_DELIVERY.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP, ARG_DELIVERY_METHOD_TYPE),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryMethodTypeParcelable =
                DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(cartTypeParcelable, args.step, deliveryMethodTypeParcelable),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
                navArgument(ARG_DELIVERY_METHOD_TYPE) {
                    type = NavType.EnumType(DeliveryMethodTypeParcelable::class.java)
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryTypeParcelable = DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
            putParcelable(ARG_DELIVERY_METHOD_TYPE, deliveryTypeParcelable)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
            val deliveryMethodType: DeliveryMethodType,
        )
    }

    data object SelectedPickupPoint : Destination<SelectedPickupPoint.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"
        const val ARG_DELIVERY_METHOD_TYPE = "arg_delivery_method_type"
        const val ARG_PICKUP_POINT_ID = "arg_pickup_point_id"

        private val routeBase: String
            get() = BaseRoute.SELECTED_PICKUP_POINT.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(
                    ARG_KEY_CART_TYPE,
                    ARG_KEY_STEP,
                    ARG_DELIVERY_METHOD_TYPE,
                    ARG_PICKUP_POINT_ID,
                ),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryMethodTypeParcelable =
                DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(
                    cartTypeParcelable,
                    args.step,
                    deliveryMethodTypeParcelable,
                    args.pickupPointId.value,
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CART_TYPE) {
                    type = NavType.EnumType(CartTypeParcelable::class.java)
                },
                navArgument(ARG_KEY_STEP) { type = NavType.IntType },
                navArgument(ARG_DELIVERY_METHOD_TYPE) {
                    type = NavType.EnumType(DeliveryMethodTypeParcelable::class.java)
                },
                navArgument(ARG_PICKUP_POINT_ID) { type = NavType.LongType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            val deliveryTypeParcelable = DeliveryMethodTypeParcelable.from(args.deliveryMethodType)
            putParcelable(ARG_KEY_CART_TYPE, cartTypeParcelable)
            putInt(ARG_KEY_STEP, args.step)
            putParcelable(ARG_DELIVERY_METHOD_TYPE, deliveryTypeParcelable)
            putLong(ARG_PICKUP_POINT_ID, args.pickupPointId.value)
        }

        data class Args(
            val cartType: CartType,
            val step: Int,
            val deliveryMethodType: DeliveryMethodType,
            val pickupPointId: PickupPoint.Id,
        )
    }
}
