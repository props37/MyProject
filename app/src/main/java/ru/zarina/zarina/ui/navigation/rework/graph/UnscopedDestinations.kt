package ru.zarina.zarina.ui.navigation.rework.graph

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.OptionalNavArg
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.rework.base.navtype.CityParcelableType

object UnscopedDestinations {
    data object Onboarding : SimpleDestination(BaseRouteReworked.ONBOARDING)

    data object CitySelector : Destination<CitySelector.Args>() {
        const val ARG_KEY_CITY = "arg_city"

        const val RESULT_KEY = "city_selector_result"

        private val routeBase: String
            get() = BaseRouteReworked.CITY_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                optionalArgNames = arrayOf(ARG_KEY_CITY),
            )

        override fun createRoute(args: Args): String {
            val cityParcelable = args.city?.let { CityParcelable.fromCity(it) }
            val cityParcelableString = Uri.encode(Json.encodeToString(cityParcelable))
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                optionalArgs = arrayOf(OptionalNavArg(ARG_KEY_CITY, cityParcelableString)),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CITY) {
                    type = NavType.CityParcelableType
                    nullable = true
                },
            )

        data class Args(val city: City? = null)

        @Parcelize
        data class Result(val city: CityParcelable) : Parcelable
    }
}
