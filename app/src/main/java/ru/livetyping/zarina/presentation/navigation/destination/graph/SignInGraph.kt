package ru.livetyping.zarina.presentation.navigation.destination.graph

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

data object SignInGraph : SimpleGraph(
    baseRoute = BaseRoute.SIGN_IN_GRAPH,
    startDestination = SignIn,
) {
    data object SignIn : SimpleDestination(BaseRoute.SIGN_IN)

    data object PasswordRecovery : SimpleDestination(BaseRoute.PASSWORD_RECOVERY)

    data object Otp : Destination<Otp.Args>() {
        const val ARG_KEY_PHONE = "arg_phone"

        private val routeBase: String
            get() = BaseRoute.SIGN_IN_OTP.route

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

    @Serializable
    data class PhoneNumberConfirmation(val phone: String)
}
