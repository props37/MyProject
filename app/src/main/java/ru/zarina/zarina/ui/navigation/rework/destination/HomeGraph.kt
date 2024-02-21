package ru.zarina.zarina.ui.navigation.rework.destination

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked

data object HomeGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.HOME_GRAPH,
    startDestination = Home,
) {
    data object Home : SimpleDestination(BaseRouteReworked.HOME)
}
