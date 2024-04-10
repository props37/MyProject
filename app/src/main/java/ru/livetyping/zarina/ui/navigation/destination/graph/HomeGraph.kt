package ru.livetyping.zarina.ui.navigation.destination.graph

import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleGraph

data object HomeGraph : SimpleGraph(
    baseRoute = BaseRoute.HOME_GRAPH,
    startDestination = Home,
) {
    data object Home : SimpleDestination(BaseRoute.HOME)
}
