package ru.zarina.zarina.ui.navigation.rework.destination.graph

import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleGraph
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked

data object CartGraph : SimpleGraph(
    baseRoute = BaseRouteReworked.CART_GRAPH,
    startDestination = Cart,
) {
    data object Cart : SimpleDestination(BaseRouteReworked.CART)
}
