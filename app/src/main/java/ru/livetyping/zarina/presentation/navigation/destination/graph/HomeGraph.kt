package ru.livetyping.zarina.presentation.navigation.destination.graph

import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

data object HomeGraph : SimpleGraph(
    baseRoute = BaseRoute.HOME_GRAPH,
    startDestination = Home,
) {
    data object Home : SimpleDestination(BaseRoute.HOME)
}
