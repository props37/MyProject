package ru.zarina.zarina.ui.navigation.destination.graph

import ru.zarina.zarina.ui.navigation.BaseRoute
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph

data object HomeGraph : SimpleGraph(
    baseRoute = BaseRoute.HOME_GRAPH,
    startDestination = Home,
) {
    data object Home : SimpleDestination(BaseRoute.HOME)
}
