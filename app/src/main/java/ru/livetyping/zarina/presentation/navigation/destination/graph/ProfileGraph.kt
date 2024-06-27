package ru.livetyping.zarina.presentation.navigation.destination.graph

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils
import ru.livetyping.zarina.presentation.navigation.base.ScreenResult
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph
import java.util.UUID
import ru.livetyping.zarina.domain.order.Order as DomainOrder

data object ProfileGraph : SimpleGraph(
    baseRoute = BaseRoute.PROFILE_GRAPH,
    startDestination = Profile,
) {
    data object Profile : SimpleDestination(BaseRoute.PROFILE)

    data object ProfileDetails : SimpleDestination(BaseRoute.PROFILE_DETAILS)

    data object ChangePassword : SimpleDestination(BaseRoute.CHANGE_PASSWORD)

    data object ChangeEmail : SimpleDestination(BaseRoute.CHANGE_EMAIL)

    data object ChangePhoneNumber : SimpleDestination(BaseRoute.CHANGE_PHONE_NUMBER)

    data object ChangePhoneNumberOtp : Destination<ChangePhoneNumberOtp.Args>() {
        const val ARG_KEY_PHONE = "arg_phone"

        private val routeBase: String
            get() = BaseRoute.CHANGE_PHONE_NUMBER_OTP.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_PHONE),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(args.phone.value),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PHONE) { type = NavType.StringType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putString(ARG_KEY_PHONE, args.phone.value)
        }

        data class Args(val phone: PhoneNumber)
    }

    data object SignOutConfirmation : SimpleDestination(BaseRoute.SIGN_OUT_CONFIRMATION)

    data object AccountDeletionConfirmation :
        SimpleDestination(BaseRoute.ACCOUNT_DELETION_CONFIRMATION)

    data object MyOrders : SimpleDestination(BaseRoute.MY_ORDERS)

    data object Order : Destination<Order.Args>() {
        const val ARG_KEY_ORDER_ID = "arg_order_id"

        private val baseRoute: String
            get() = BaseRoute.ORDER.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_ORDER_ID),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.orderId.value),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_ORDER_ID) { type = NavType.LongType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putLong(ARG_KEY_ORDER_ID, args.orderId.value)
        }

        data class Args(val orderId: DomainOrder.Id)
    }

    data object OrderCancellation : Destination<OrderCancellation.Args>() {
        const val ARG_KEY_ORDER_ID = "arg_order_id"

        const val RESULT_KEY = "result_order_cancellation"

        private val baseRoute: String
            get() = BaseRoute.ORDER_CANCELLATION.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_ORDER_ID),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.orderId.value),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_ORDER_ID) { type = NavType.LongType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putLong(ARG_KEY_ORDER_ID, args.orderId.value)
        }

        data class Args(val orderId: DomainOrder.Id)

        @Parcelize
        data class Result(
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object Stores : SimpleDestination(BaseRoute.STORES)
}
