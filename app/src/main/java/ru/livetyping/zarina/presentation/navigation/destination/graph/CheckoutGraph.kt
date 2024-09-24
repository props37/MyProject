package ru.livetyping.zarina.presentation.navigation.destination.graph

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.checkout.CheckoutParamsParcelable
import ru.livetyping.zarina.presentation.model.checkout.CustomerParcelable
import ru.livetyping.zarina.presentation.model.checkout.DeliveryDateTimePeriodParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.Graph
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils
import ru.livetyping.zarina.presentation.navigation.base.ScreenResult
import ru.livetyping.zarina.presentation.navigation.navtype.CartProductParcelableListType
import ru.livetyping.zarina.presentation.navigation.navtype.CheckoutParamsParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.CityParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.CustomerParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.DeliveryDateTimePeriodParcelableListType
import ru.livetyping.zarina.presentation.navigation.navtype.StoreParcelableType
import ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector.CourierDeliveryDateTimeSelectorType
import ru.livetyping.zarina.util.library.navigation.getTypeMapEnumTypePair
import java.util.UUID
import kotlin.reflect.KType
import kotlin.reflect.typeOf

data object CheckoutGraph : Graph<CheckoutGraph.Customer.Args>() {

    private val routeBase: String
        get() = BaseRoute.CHECKOUT_GRAPH.route

    override val routeSchema: String
        get() = RouteUtils.generateRouteSchema(
            routeBase = routeBase,
            argNames = arrayOf(Customer.ARG_KEY_CART_TYPE, Customer.ARG_KEY_STEP),
        )

    override fun createRoute(args: Customer.Args): String {
        val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
        return RouteUtils.generateRoute(
            routeBase = routeBase,
            args = arrayOf(cartTypeParcelable, args.step),
        )
    }

    override val arguments: List<NamedNavArgument>
        get() = Customer.arguments

    override fun createArgsBundle(args: Customer.Args): Bundle {
        return Customer.createArgsBundle(args)
    }

    override val startDestination = Customer



    data object Customer : Destination<Customer.Args>() {
        const val ARG_KEY_CART_TYPE = "arg_cart_type"
        const val ARG_KEY_STEP = "arg_step"

        private val routeBase: String
            get() = BaseRoute.CUSTOMER.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_CART_TYPE, ARG_KEY_STEP),
            )

        override fun createRoute(args: Args): String {
            val cartTypeParcelable = CartTypeParcelable.from(args.cartType)
            return RouteUtils.generateRoute(routeBase = routeBase,
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

    @Serializable
    data class PickupStoreSelection(
        val cartType: CartTypeParcelable,
        val step: Int,
        val deliveryMethodType: DeliveryMethodTypeParcelable,
        val customer: CustomerParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CartTypeParcelable>(),
                    getTypeMapEnumTypePair<DeliveryMethodTypeParcelable>(),
                    typeOf<CustomerParcelable>() to NavType.CustomerParcelableType,
                )
            }
        }
    }

    @Serializable
    data class SelectedPickupStore(
        val cartType: CartTypeParcelable,
        val step: Int,
        val deliveryMethodType: DeliveryMethodTypeParcelable,
        val city: CityParcelable,
        val store: StoreParcelable,
        val availableProducts: List<CartProductParcelable>,
        val customer: CustomerParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CartTypeParcelable>(),
                    getTypeMapEnumTypePair<DeliveryMethodTypeParcelable>(),
                    typeOf<CityParcelable>() to NavType.CityParcelableType,
                    typeOf<StoreParcelable>() to NavType.StoreParcelableType,
                    typeOf<List<CartProductParcelable>>() to NavType.CartProductParcelableListType,
                    typeOf<CustomerParcelable>() to NavType.CustomerParcelableType,
                )
            }
        }
    }

    @Serializable
    data class DeliveryMethod(
        val cartType: CartTypeParcelable,
        val step: Int,
        val customer: CustomerParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CartTypeParcelable>(),
                    typeOf<CustomerParcelable>() to NavType.CustomerParcelableType,
                )
            }
        }
    }

    @Serializable
    data class CourierDelivery(
        val cartType: CartTypeParcelable,
        val step: Int,
        val deliveryMethodType: DeliveryMethodTypeParcelable,
        val customer: CustomerParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CartTypeParcelable>(),
                    getTypeMapEnumTypePair<DeliveryMethodTypeParcelable>(),
                    typeOf<CustomerParcelable>() to NavType.CustomerParcelableType,
                )
            }
        }
    }

    @Serializable
    data class CourierDeliveryDateTimeSelector(
        val type: CourierDeliveryDateTimeSelectorType,
        val deliveryOptionId: String,
        val dateTimePeriods: List<DeliveryDateTimePeriodParcelable>,
    ) {
        @Parcelize
        data class Result(
            val deliveryOptionId: String,
            val selectorType: CourierDeliveryDateTimeSelectorType,
            val dateTimePeriod: DeliveryDateTimePeriodParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable

        companion object {
            const val RESULT_KEY = "courier_delivery_date_time_selector_result"

            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CourierDeliveryDateTimeSelectorType>(),
                    typeOf<List<DeliveryDateTimePeriodParcelable>>() to
                            NavType.DeliveryDateTimePeriodParcelableListType,
                )
            }
        }
    }

    @Serializable
    data class PostDelivery(
        val cartType: CartTypeParcelable,
        val step: Int,
        val deliveryMethodType: DeliveryMethodTypeParcelable,
        val customer: CustomerParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    getTypeMapEnumTypePair<CartTypeParcelable>(),
                    getTypeMapEnumTypePair<DeliveryMethodTypeParcelable>(),
                    typeOf<CustomerParcelable>() to NavType.CustomerParcelableType,
                )
            }
        }
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

    @Serializable
    data class OrderPlacing(
        val step: Int,
        val checkoutParams: CheckoutParamsParcelable,
    ) {
        companion object {
            fun typeMap(): Map<KType, NavType<*>> {
                return mapOf(
                    typeOf<CheckoutParamsParcelable>() to NavType.CheckoutParamsParcelableType,
                )
            }
        }
    }
}
