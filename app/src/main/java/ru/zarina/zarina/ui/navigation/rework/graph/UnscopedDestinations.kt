package ru.zarina.zarina.ui.navigation.rework.graph

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked

object UnscopedDestinations {
    data object Onboarding : SimpleDestination(BaseRouteReworked.ONBOARDING)

    data object CitySelector : SimpleDestination(BaseRouteReworked.CITY_SELECTOR)
}
