package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object ProfileGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.PROFILE_GRAPH,
    startDestination = Profile,
) {
    data object Profile : SimpleDestination(BaseRouteReworked.PROFILE)
}
