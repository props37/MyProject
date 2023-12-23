package ru.zarina.zarina.ui.navigation.rework.graph

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked

data object ProfileGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.PROFILE_GRAPH,
    startDestination = Profile,
) {
    data object Profile : SimpleDestination(BaseRouteReworked.PROFILE)
}
